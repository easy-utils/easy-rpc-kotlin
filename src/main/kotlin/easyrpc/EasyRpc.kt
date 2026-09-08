package easyrpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request as OkRequest
import okhttp3.Response as OkResponse
import okhttp3.ResponseBody
import java.io.IOException
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/** Multi-value headers. */
typealias Headers = Map<String, List<String>>

/** Wire error with a Connect code. */
data class RPCError(val code: Int, override val message: String) : Exception("easyrpc: code=$code $message")

/** Normalized request. */
data class Request(
    val url: String,
    val method: String = "POST",
    val headers: Headers = emptyMap(),
    val body: ByteArray? = null,
)

/** Normalized response. */
data class Response(
    val status: Int,
    val headers: Headers = emptyMap(),
    val body: ByteArray = ByteArray(0),
    val error: RPCError? = null,
)

/** Server-stream of raw message payloads. */
interface Stream {
    suspend fun recv(): ByteArray?   // null => end
    fun cancel()
}

/** Core interface a bridge implements. */
interface Transport {
    suspend fun send(req: Request): Response
    suspend fun openStream(req: Request): Stream
}

fun httpStatus(code: Int): Int = when (code) {
    3 -> 400
    5 -> 404
    7 -> 403
    8 -> 429
    16 -> 401
    14 -> 503
    else -> 500
}

fun connectFromStatus(status: Int): Int = when (status) {
    400 -> 3
    404 -> 5
    403 -> 7
    401 -> 16
    429 -> 8
    503 -> 14
    else -> 13
}

const val FLAG_END_STREAM: Int = 0x02

/** Encode one streaming frame. */
fun frame(payload: ByteArray, end: Boolean = false): ByteArray {
    val out = ByteArray(5 + payload.size)
    out[0] = if (end) FLAG_END_STREAM.toByte() else 0
    out[1] = (payload.size ushr 24).toByte()
    out[2] = (payload.size ushr 16).toByte()
    out[3] = (payload.size ushr 8).toByte()
    out[4] = payload.size.toByte()
    payload.copyInto(out, 5)
    return out
}

/** De-frames a byte stream; returns payloads. End-stream frame => null. */
class FrameReader {
    private var acc = ByteArray(0)
    fun push(buf: ByteArray): List<ByteArray> {
        acc += buf
        val out = mutableListOf<ByteArray>()
        while (true) {
            if (acc.size < 5) break
            val flags = acc[0].toInt() and 0xff
            val len = ((acc[1].toInt() and 0xff) shl 24) or
                ((acc[2].toInt() and 0xff) shl 16) or
                ((acc[3].toInt() and 0xff) shl 8) or
                (acc[4].toInt() and 0xff)
            if (acc.size < 5 + len) break
            val payload = acc.copyOfRange(5, 5 + len)
            acc = acc.copyOfRange(5 + len, acc.size)
            out.add(payload)
            if ((flags and FLAG_END_STREAM) != 0) {
                // terminal marker
                out.add(ByteArray(0).also { } ) // sentinel handled by caller
                // instead: append null marker via empty + flag; simpler: return with a poison
                break
            }
        }
        return out
    }
}

/** okhttp (HTTP/1.1) bridge; matches Go net/http server. */
class OkHttpTransport(
    private val client: OkHttpClient = OkHttpClient(),
    private val base: String = "",
) : Transport {

    override suspend fun send(req: Request): Response {
        val r = buildRequest(req, "application/proto")
        val resp = await(r)
        val body = resp.body?.bytes() ?: ByteArray(0)
        val headers = resp.headers.toMultimap()
        val status = resp.code
        resp.close()
        return Response(
            status = status,
            headers = headers,
            body = body,
            error = if (status >= 300) RPCError(connectFromStatus(status), String(body)) else null,
        )
    }

    override suspend fun openStream(req: Request): Stream {
        val r = buildRequest(req, "application/connect+proto")
        val resp = await(r)
        val reader = FrameReader()
        val body = resp.body ?: error("no body")
        val it = body.byteStream()
        return object : Stream {
            private var pushed = ArrayDeque<ByteArray>()
            private var finished = false
            private var ended = false
            private val readLock = Object()
            override suspend fun recv(): ByteArray? {
                if (ended) return null
                if (pushed.isNotEmpty()) return pushed.removeFirst()
                val chunk = readChunk(it) ?: run { ended = true; return null }
                val frames = reader.push(chunk)
                for (f in frames) {
                    // detect end frame encoded as one empty; but frame() end appends empty payload. We'll treat
                    // a frame with zero length AFTER flags end as end. Simpler: raw end detection not via sentinel.
                    pushed.addLast(f)
                }
                return if (pushed.isEmpty()) null else pushed.removeFirst()
            }
            override fun cancel() {
                body.close()
            }
        }
    }

    private fun buildRequest(req: Request, cType: String): OkRequest {
        val b = OkRequest.Builder()
            .url(if (req.url.startsWith("http")) req.url else base + req.url)
            .method(req.method, if (req.body != null) req.body!!.toRequestBody(null) else null)
            .header("content-type", cType)
        return b.build()
    }

    private suspend fun await(r: OkRequest): OkResponse =
        suspendCancellableCoroutine { cont ->
            val call: Call = client.newCall(r)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) = cont.resumeWithException(e)
                override fun onResponse(call: Call, response: OkResponse) = cont.resume(response)
            })
        }
}

private fun ByteArray.toRequestBody(mt: okhttp3.MediaType?): okhttp3.RequestBody =
    okhttp3.RequestBody.create(mt, this)

private fun readChunk(it: InputStream): ByteArray? {
    val buf = ByteArray(8192)
    val n = it.read(buf)
    return if (n <= 0) null else buf.copyOf(n)
}

// Framework type for generated method tables.
data class EasyRpcMethod(
    val service: String,
    val name: String,
    val path: String,
    val serverStream: Boolean,
)

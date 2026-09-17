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
    /** Set when the stream ended with a Connect end-stream error. */
    fun lastError(): RPCError? = null
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

/** Connect code -> stable lowercase wire name. */
val CODE_NAMES = mapOf(
    0 to "ok", 1 to "canceled", 2 to "unknown", 3 to "invalid_argument",
    4 to "deadline_exceeded", 5 to "not_found", 6 to "already_exists",
    7 to "permission_denied", 8 to "resource_exhausted", 9 to "failed_precondition",
    10 to "aborted", 11 to "out_of_range", 12 to "unimplemented", 13 to "internal",
    14 to "unavailable", 15 to "data_loss", 16 to "unauthenticated",
)
fun codeToString(code: Int): String = CODE_NAMES[code] ?: "unknown"
fun codeFromString(name: String): Int = CODE_NAMES.entries.firstOrNull { it.value == name }?.key ?: 2

/** Encode a Connect end-stream payload; a clean end is empty. */
fun encodeEndStream(code: Int, message: String): ByteArray {
    if (code == 0) return ByteArray(0)
    val escaped = message.replace("\\", "\\\\").replace("\"", "\\\"")
    return "{\"error\":{\"code\":\"${codeToString(code)}\",\"message\":\"$escaped\"}}"
        .toByteArray(Charsets.UTF_8)
}

/** Decode a Connect end-stream payload into (code, message); (0, "") clean. */
fun decodeEndStream(payload: ByteArray): Pair<Int, String> {
    if (payload.isEmpty()) return 0 to ""
    val text = payload.toString(Charsets.UTF_8)
    fun field(name: String): String? {
        val key = "\"$name\""
        val i = text.indexOf(key); if (i < 0) return null
        val c = text.indexOf(':', i + key.length); if (c < 0) return null
        val q = text.indexOf('"', c + 1); if (q < 0) return null
        val sb = StringBuilder(); var j = q + 1
        while (j < text.length) {
            val ch = text[j]
            if (ch == '\\') { if (j + 1 < text.length) { sb.append(text[j + 1]); j += 2; continue } }
            if (ch == '"') break
            sb.append(ch); j++
        }
        return sb.toString()
    }
    val name = field("code") ?: return 0 to ""
    return codeFromString(name) to (field("message") ?: "")
}

/** Reconstruct the exact RPCError from the server's connect-code/connect-error
 *  headers (the HTTP status alone is lossy). */
fun rpcErrorFrom(status: Int, headers: Map<String, List<String>>, body: ByteArray): RPCError {
    val code = headers.entries.firstOrNull { it.key.equals("connect-code", ignoreCase = true) }?.value?.firstOrNull()
    val c = code?.toIntOrNull()
    if (c != null) {
        val msg = headers.entries.firstOrNull { it.key.equals("connect-error", ignoreCase = true) }?.value?.firstOrNull() ?: ""
        return RPCError(c, msg)
    }
    val (jc, jm) = decodeErrorJson(body)
    if (jc != 0) return RPCError(jc, jm)
    return RPCError(connectFromStatus(status), String(body))
}

/** gzip-compress (identity on failure). */
fun gzipCompress(data: ByteArray): ByteArray = try {
    val bos = java.io.ByteArrayOutputStream()
    java.util.zip.GZIPOutputStream(bos).use { it.write(data) }
    bos.toByteArray()
} catch (e: Exception) { data }

/** gzip-decompress (identity on failure). */
fun gzipDecompress(data: ByteArray): ByteArray = try {
    java.util.zip.GZIPInputStream(java.io.ByteArrayInputStream(data)).use { it.readBytes() }
} catch (e: Exception) { data }

const val HEADER_TIMEOUT = "connect-timeout-ms"
const val HEADER_PROTOCOL_VERSION = "connect-protocol-version"
const val HEADER_ACCEPT_ENCODING = "connect-accept-encoding"
const val ENCODING_GZIP = "gzip"
const val COMPRESS_MIN_BYTES = 1024
const val CONNECT_PROTOCOL_VERSION = "1"
const val DEFAULT_MAX_MESSAGE_BYTES = 4 * 1024 * 1024

/** Parse the Connect timeout header into milliseconds (0 = none). */
fun parseTimeout(value: String?): Int {
    val n = value?.toIntOrNull() ?: return 0
    return if (n > 0) n else 0
}

/** Attach a deadline to a request. */
fun withTimeout(req: Request, timeoutMs: Int): Request {
    if (timeoutMs <= 0) return req
    return req.copy(headers = req.headers + (HEADER_TIMEOUT to listOf(timeoutMs.toString())))
}

/** Connect unary error body `{code,message}`. */
fun encodeErrorJson(code: Int, message: String): ByteArray =
    "{\"code\":\"${codeToString(code)}\",\"message\":\"${message.replace("\\","\\\\").replace("\"","\\\"")}\"}"
        .toByteArray(Charsets.UTF_8)

/** Parse a Connect unary error body; (0, "") when not an error body. */
fun decodeErrorJson(body: ByteArray): Pair<Int, String> {
    if (body.isEmpty()) return 0 to ""
    val text = body.toString(Charsets.UTF_8)
    val key = "\"code\""
    val i = text.indexOf(key); if (i < 0) return 0 to ""
    val c = text.indexOf(':', i + key.length); if (c < 0) return 0 to ""
    val q = text.indexOf('"', c + 1); if (q < 0) return 0 to ""
    val e = text.indexOf('"', q + 1); if (e < 0) return 0 to ""
    return codeFromString(text.substring(q + 1, e)) to ""
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

/** One decoded frame: payload + whether it is the END frame. */
data class Frame(val payload: ByteArray, val end: Boolean)

/** De-frames a byte stream into typed frames. */
class FrameReader {
    private var acc = ByteArray(0)
    fun push(buf: ByteArray): List<Frame> {
        acc += buf
        val out = mutableListOf<Frame>()
        while (true) {
            if (acc.size < 5) break
            val flags = acc[0].toInt() and 0xff
            val len = ((acc[1].toInt() and 0xff) shl 24) or
                ((acc[2].toInt() and 0xff) shl 16) or
                ((acc[3].toInt() and 0xff) shl 8) or
                (acc[4].toInt() and 0xff)
            if (acc.size < 5 + len) break
            var payload = acc.copyOfRange(5, 5 + len)
            acc = acc.copyOfRange(5 + len, acc.size)
            if ((flags and 0x01) != 0) payload = gzipDecompress(payload)
            out.add(Frame(payload, (flags and FLAG_END_STREAM) != 0))
        }
        return out
    }
}

/** A call interceptor: mutate the request (auth/metadata), impose a deadline,
 *  observe, or short-circuit. `next_` performs the call. */
interface Interceptor {
    suspend fun unary(req: Request, next_: suspend (Request) -> Response): Response = next_(req)
    suspend fun stream(req: Request, next_: suspend (Request) -> Stream): Stream = next_(req)
}

/** Apply interceptors (first = outermost) around a Transport. */
class InterceptorTransport(private val ics: List<Interceptor>, private val inner: Transport) : Transport {
    override suspend fun send(req: Request): Response {
        suspend fun dispatch(i: Int, r: Request): Response =
            if (i >= ics.size) inner.send(r) else ics[i].unary(r) { nr -> dispatch(i + 1, nr) }
        return dispatch(0, req)
    }
    override suspend fun openStream(req: Request): Stream {
        suspend fun dispatch(i: Int, r: Request): Stream =
            if (i >= ics.size) inner.openStream(r) else ics[i].stream(r) { nr -> dispatch(i + 1, nr) }
        return dispatch(0, req)
    }
}

/** Attach fixed metadata to every call. */
class MetadataInterceptor(private val md: Map<String, List<String>>) : Interceptor {
    private fun aug(req: Request): Request {
        val h = req.headers.toMutableMap()
        for ((k, v) in md) h.putIfAbsent(k, v)
        return req.copy(headers = h)
    }
    override suspend fun unary(req: Request, next_: suspend (Request) -> Response) = next_(aug(req))
    override suspend fun stream(req: Request, next_: suspend (Request) -> Stream) = next_(aug(req))
}

/** Attach a Connect deadline to every call. */
class TimeoutInterceptor(private val ms: Int) : Interceptor {
    override suspend fun unary(req: Request, next_: suspend (Request) -> Response) = next_(withTimeout(req, ms))
    override suspend fun stream(req: Request, next_: suspend (Request) -> Stream) = next_(withTimeout(req, ms))
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
            error = if (status >= 300) rpcErrorFrom(status, headers, body) else null,
        )
    }

    override suspend fun openStream(req: Request): Stream {
        val r = buildRequest(req, "application/connect+proto")
        val resp = await(r)
        val reader = FrameReader()
        val body = resp.body ?: error("no body")
        val it = body.byteStream()
        return object : Stream {
            private val pushed = ArrayDeque<ByteArray>()
            private var ended = false
            private var err: RPCError? = null
            override suspend fun recv(): ByteArray? {
                if (ended) return null
                if (pushed.isNotEmpty()) return pushed.removeFirst()
                val chunk = readChunk(it) ?: run { ended = true; return null }
                for (f in reader.push(chunk)) {
                    if (f.end) {
                        val (code, message) = decodeEndStream(f.payload)
                        if (code != 0) err = RPCError(code, message)
                        ended = true
                        break
                    }
                    pushed.addLast(f.payload)
                }
                return if (pushed.isEmpty()) null else pushed.removeFirst()
            }
            override fun lastError(): RPCError? = err
            override fun cancel() {
                body.close()
            }
        }
    }

    private fun buildRequest(req: Request, cType: String): OkRequest {
        val b = OkRequest.Builder()
            .url(if (req.url.startsWith("http")) req.url else base + req.url)
            .method(req.method, if (req.body != null) req.body!!.toRequestBody(null) else null)
        // Caller-supplied metadata (auth/tenant/token) first, then content-type.
        for ((k, vs) in req.headers) {
            for (v in vs) b.addHeader(k, v)
        }
        b.header("content-type", cType)
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

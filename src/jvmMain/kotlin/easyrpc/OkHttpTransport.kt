package easyrpc

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request as OkRequest
import okhttp3.Response as OkResponse
import java.io.IOException
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/** Platform default on the JVM: OkHttp (HTTP/1.1, matches Go net/http). */
actual fun defaultTransport(base: String): Transport = OkHttpTransport(base = base)

/** okhttp (HTTP/1.1) bridge. HTTP-layer gzip (unary) is handled here; the
 *  frame-layer gzip is handled by [decodeFrame] via the shared core. */
class OkHttpTransport(
    private val client: OkHttpClient = OkHttpClient(),
    private val base: String = "",
) : Transport {

    override suspend fun send(req: Request): Response {
        val r = buildRequest(req, CONTENT_TYPE_UNARY)
        val resp = await(r)
        var body = resp.body?.bytes() ?: ByteArray(0)
        val allHeaders = resp.headers.toMultimap()
        val status = resp.code
        resp.close()
        if ((allHeaders.entries.firstOrNull { it.key.equals("content-encoding", true) }?.value?.firstOrNull()) == ENCODING_GZIP && body.isNotEmpty()) {
            body = gzipDecompress(body)
        }
        val (headers, trailers) = demuxTrailers(allHeaders)
        return Response(
            status = status,
            headers = headers,
            body = body,
            trailers = trailers,
            error = if (status >= 300) rpcErrorFrom(status, headers, body) else null,
        )
    }

    override suspend fun openStream(req: Request): Stream {
        val r = buildRequest(req, CONTENT_TYPE_STREAM)
        val resp = await(r)
        val reader = FrameReader()
        val body = resp.body ?: error("no body")
        val it = body.byteStream()
        return object : Stream {
            private val pushed = ArrayDeque<ByteArray>()
            private var ended = false
            private var err: RPCError? = null
            private var trailers: Headers = emptyMap()
            override suspend fun recv(): ByteArray? {
                // Drain buffered frames first, even after the END frame has
                // been seen (frames emitted before the error must not drop).
                if (pushed.isNotEmpty()) return pushed.removeFirst()
                if (ended) return null
                val chunk = readChunk(it) ?: run {
                    ended = true
                    // Fault matrix F2/M8: missing END frame or trailing
                    // partial bytes = truncated mid-stream.
                    try {
                        reader.finish()
                    } catch (e: RPCError) {
                        err = e
                    }
                    return null
                }
                for (f in reader.push(chunk)) {
                    val d = decodeFrame(f)
                    if (f.end) {
                        if (d.trailers.isNotEmpty()) trailers = d.trailers
                        err = d.error
                        ended = true
                        break
                    }
                    pushed.addLast(d.body)
                }
                return if (pushed.isEmpty()) null else pushed.removeFirst()
            }
            override fun lastError(): RPCError? = err
            override fun trailers(): Headers = trailers
            override fun cancel() {
                body.close()
            }
        }
    }

    private fun buildRequest(req: Request, cType: String): OkRequest {
        val b = OkRequest.Builder()
            .url(if (req.url.startsWith("http")) req.url else base + req.url)
            .method("POST", if (req.body != null) req.body!!.toRequestBody(null) else null)
        b.header("Accept-Encoding", ENCODING_GZIP)
        // Caller-supplied metadata (auth/tenant/token) first.
        for ((k, vs) in req.headers) {
            for (v in vs) b.addHeader(k, v)
        }
        // Default content-type ONLY when the caller did not set one.
        val hasCt = req.headers.keys.any { it.equals("content-type", ignoreCase = true) }
        if (!hasCt) b.header("content-type", cType)
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

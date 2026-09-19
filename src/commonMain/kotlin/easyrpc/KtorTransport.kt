package easyrpc

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readRawBytes
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpMethod
import io.ktor.utils.io.readAvailable

/**
 * Multiplatform Ktor transport (commonMain). A platform `defaultTransport`
 * provides the engine-specific [HttpClient]. HTTP-layer gzip (unary
 * `Content-Encoding`) is the engine's / Ktor's responsibility; the
 * *frame-layer* gzip (server-stream flag bit0) is handled here via
 * [gzipDecompress] — the core owns framing, never `Content-Encoding`.
 */
class KtorTransport(
    private val client: HttpClient,
    private val base: String = "",
) : Transport {

    private fun url(u: String): String =
        if (u.startsWith("http://") || u.startsWith("https://")) u else base + u

    private fun HttpRequestBuilder.applyReq(req: Request, defaultCt: String, stream: Boolean) {
        method = HttpMethod.Post
        url(url(req.url))
        for ((k, vs) in req.headers) for (v in vs) header(k, v)
        val hasCt = req.headers.keys.any { it.equals("content-type", ignoreCase = true) }
        if (!hasCt) header("content-type", defaultCt)
        if (stream) {
            header("Connect-Accept-Encoding", ENCODING_GZIP)
            header("connect-protocol-version", CONNECT_PROTOCOL_VERSION)
        } else {
            header("Accept-Encoding", ENCODING_GZIP)
            header("connect-protocol-version", CONNECT_PROTOCOL_VERSION)
        }
        req.body?.let { setBody(it) }
    }

    override suspend fun send(req: Request): Response {
        val resp = client.request(url(req.url)) { applyReq(req, CONTENT_TYPE_UNARY, stream = false) }
        var body = resp.readRawBytes()
        val status = resp.status.value
        val all: Headers = resp.headers.entries().associate { (k, _) ->
            k.lowercase() to (resp.headers.getAll(k) ?: emptyList())
        }
        // Unary HTTP-layer gzip is normally decompressed by the engine; if the
        // engine left it (native/no-auto-decompress), handle it defensively.
        if ((all["content-encoding"]?.firstOrNull() == ENCODING_GZIP) && body.isNotEmpty()) {
            body = gzipDecompress(body)
        }
        val (hdrs, trailers) = demuxTrailers(all)
        return Response(
            status = status,
            headers = hdrs,
            body = body,
            trailers = trailers,
            error = if (status >= 300) rpcErrorFrom(status, hdrs, body) else null,
        )
    }

    override suspend fun openStream(req: Request): Stream {
        val resp: HttpResponse = client.request(url(req.url)) { applyReq(req, CONTENT_TYPE_STREAM, stream = true) }
        if (resp.status.value >= 300) {
            val body = resp.readRawBytes()
            throw rpcErrorFrom(resp.status.value, emptyMap(), body)
        }
        val channel = resp.bodyAsChannel()
        return KtorStream(channel)
    }
}

/** Stream reader over a Ktor [io.ktor.utils.io.ByteReadChannel]. */
internal class KtorStream(private val channel: io.ktor.utils.io.ByteReadChannel) : Stream {
    private val reader = FrameReader()
    private val pushed = ArrayDeque<ByteArray>()
    private var ended = false
    private var err: RPCError? = null
    private var trailers: Headers = emptyMap()
    private val buf = ByteArray(8192)

    override suspend fun recv(): ByteArray? {
        while (true) {
            if (pushed.isNotEmpty()) return pushed.removeFirst()
            if (ended) return null
            val n = channel.readAvailable(buf, 0, buf.size)
            if (n <= 0) {
                ended = true
                try {
                    reader.finish()
                } catch (e: RPCError) {
                    err = e
                }
                return null
            }
            for (f in reader.push(buf.copyOf(n))) {
                val d = decodeFrame(f)
                if (f.end) {
                    if (d.trailers.isNotEmpty()) trailers = d.trailers
                    err = d.error
                    ended = true
                    break
                }
                pushed.addLast(d.body)
            }
        }
    }

    override fun lastError(): RPCError? = err
    override fun trailers(): Headers = trailers
    override fun cancel() {
        channel.cancel(null)
    }
}

package easyrpc

import easyrpc.Frame
import easyrpc.FrameReader
import easyrpc.RPCError
import easyrpc.decodeEndStream

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Ktor CIO transport — minimal, pure-Kotlin, HTTP/1.1 only.
 *
 * This is the lightweight fallback for non-JVM/native targets (and any target
 * that needs the smallest dependency footprint). It deliberately does NOT do
 * HTTP/2 or HTTP/3; use OkHttpTransport (JVM, h1+h2c+h2), CronetTransport
 * (Android, h1+h2+h3) or DarwinTransport (iOS, h1+h2+h3) for those.
 */
class CioTransport(
    private val base: String = "",
    client: HttpClient = HttpClient(CIO),
) : Transport {

    private val client: HttpClient = client

    private fun url(u: String): String =
        if (u.startsWith("http://") || u.startsWith("https://")) u else base + u

    override suspend fun send(req: Request): Response {
        val resp = client.request(url(req.url)) {
            method = HttpMethod.Post
            req.headers.forEach { (k, vs) -> vs.forEach { v -> headers.append(k, v) } }
            // Default content-type ONLY when the caller did not set one — a
            // hardcoded proto value silently breaks the JSON codec.
            val hasCt = req.headers.keys.any { it.equals("content-type", ignoreCase = true) }
            if (!hasCt) header(HttpHeaders.ContentType, "application/proto")
            header(HttpHeaders.AcceptEncoding, "gzip")
            req.body?.let { setBody(it) }
        }
        var body = resp.readRawBytes()
        val status = resp.status.value
        // Real header map + shared error fallback chain (code/details survive).
        val all: Map<String, List<String>> = resp.headers.entries().associate { (k, _) -> k.lowercase() to (resp.headers.getAll(k) ?: emptyList()) }
        if ((all["content-encoding"]?.firstOrNull()) == "gzip" && body.isNotEmpty()) body = gzipDecompress(body)
        val (hdrs, trailers) = demuxTrailers(all)
        return Response(status, hdrs, body, trailers,
            if (status >= 300) rpcErrorFrom(status, hdrs, body) else null)
    }

    override suspend fun openStream(req: Request): Stream {
        val resp = client.request(url(req.url)) {
            method = HttpMethod.Post
            req.headers.forEach { (k, vs) -> vs.forEach { v -> headers.append(k, v) } }
            val hasCt2 = req.headers.keys.any { it.equals("content-type", ignoreCase = true) }
            if (!hasCt2) header(HttpHeaders.ContentType, "application/connect+proto")
            header("Connect-Accept-Encoding", "gzip")
            req.body?.let { setBody(it) }
        }
        val bodyBytes = resp.readRawBytes()
        // Shared FrameReader semantics: the END frame terminates the stream
        // (never yielded as a payload), end-stream errors surface via
        // lastError(), and finish() catches truncated bodies.
        val reader = FrameReader()
        var frames = reader.push(bodyBytes)
        var err: RPCError? = null
        var trailers: Map<String, List<String>> = emptyMap()
        val endedIdx = frames.indexOfFirst { it.end }
        if (endedIdx >= 0) {
            val endFrame = frames[endedIdx]
            val es = decodeEndStream(endFrame.payload)
            if (es.metadata.isNotEmpty()) trailers = es.metadata
            if (es.code != 0) err = RPCError(es.code, es.message, es.details)
            frames = frames.subList(0, endedIdx)
        } else {
            try { reader.finish() } catch (e: RPCError) { err = e }
        }
        return object : Stream {
            private val pushed = ArrayDeque(frames.map { it.payload })
            override suspend fun recv(): ByteArray? = pushed.removeFirstOrNull()
            override fun lastError(): RPCError? = err
            override fun trailers(): Map<String, List<String>> = trailers
            override fun cancel() {}
        }
    }
}

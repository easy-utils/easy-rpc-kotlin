package easyrpc

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
            method = HttpMethod.parse(req.method)
            req.headers.forEach { (k, vs) -> vs.forEach { v -> headers.append(k, v) } }
            // Default content-type ONLY when the caller did not set one — a
            // hardcoded proto value silently breaks the JSON codec.
            val hasCt = req.headers.keys.any { it.equals("content-type", ignoreCase = true) }
            if (!hasCt) header(HttpHeaders.ContentType, "application/proto")
            req.body?.let { setBody(it) }
        }
        val body = resp.readRawBytes()
        val status = resp.status.value
        // Real header map + shared error fallback chain (code/details survive).
        val hdrs: Map<String, List<String>> = resp.headers.entries().associate { (k, _) -> k.lowercase() to (resp.headers.getAll(k) ?: emptyList()) }
        return Response(status, hdrs, body,
            if (status >= 300) rpcErrorFrom(status, hdrs, body) else null)
    }

    override suspend fun openStream(req: Request): Stream {
        val resp = client.request(url(req.url)) {
            method = HttpMethod.parse(req.method)
            req.headers.forEach { (k, vs) -> vs.forEach { v -> headers.append(k, v) } }
            val hasCt2 = req.headers.keys.any { it.equals("content-type", ignoreCase = true) }
            if (!hasCt2) header(HttpHeaders.ContentType, "application/connect+proto")
            req.body?.let { setBody(it) }
        }
        val bodyBytes = resp.readRawBytes()
        val reader = FrameReader2()
        return object : Stream {
            private var started = false
            private val frames = reader.push(bodyBytes)
            private var idx = 0
            override suspend fun recv(): ByteArray? {
                if (idx < frames.size) return frames[idx++]
                return null
            }
            override fun cancel() {}
        }
    }
}

/** Minimal frame parser reusing the same wire framing as FrameReader but
 * returning single payloads per call (drains until one complete frame). */
private class FrameReader2 {
    private var acc = ByteArray(0)
    fun push(buf: ByteArray): List<ByteArray> {
        acc += buf
        val out = mutableListOf<ByteArray>()
        while (true) {
            if (acc.size < 5) break
            val flags = acc[0].toInt() and 0xff
            val len = ((acc[1].toInt() and 0xff) shl 24) or ((acc[2].toInt() and 0xff) shl 16) or
                ((acc[3].toInt() and 0xff) shl 8) or (acc[4].toInt() and 0xff)
            if (acc.size < 5 + len) break
            val payload = acc.copyOfRange(5, 5 + len)
            acc = acc.copyOfRange(5 + len, acc.size)
            out.add(payload)
            if ((flags and FLAG_END_STREAM) != 0) break
        }
        return out
    }
}

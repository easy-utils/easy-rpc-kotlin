package easyrpc

import io.ktor.client.HttpClient
import io.ktor.client.engine.cronet.Cronet
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Cronet client transport for Android. Backed by Cronet (Chromium network
 * stack), it covers HTTP/1 + HTTP/2 + HTTP/3 at the system level.
 *
 * Place this file in `androidMain/kotlin/easyrpc/` of a Kotlin Multiplatform
 * module and add:
 *   implementation("io.ktor:ktor-client-cronet:3.3.0")
 *   implementation("org.chromium.net:cronet-embedded:...")
 */
class CronetTransport(
    private val base: String = "",
    client: HttpClient = HttpClient(Cronet),
) : Transport {

    private val client: HttpClient = client

    private fun url(u: String): String =
        if (u.startsWith("http://") || u.startsWith("https://")) u else base + u

    override suspend fun send(req: Request): Response {
        val resp = client.request(url(req.url)) {
            method = HttpMethod.parse(req.method)
            req.headers.forEach { (k, vs) -> vs.forEach { v -> headers.append(k, v) } }
            header(HttpHeaders.ContentType, "application/proto")
            req.body?.let { setBody(it) }
        }
        val body = resp.readRawBytes()
        val status = resp.status.value
        return Response(status, emptyMap(), body,
            if (status >= 300) RPCError(connectFromStatus(status), body.toString(Charsets.UTF_8)) else null)
    }

    override suspend fun openStream(req: Request): Stream {
        val resp = client.request(url(req.url)) {
            method = HttpMethod.parse(req.method)
            req.headers.forEach { (k, vs) -> vs.forEach { v -> headers.append(k, v) } }
            header(HttpHeaders.ContentType, "application/connect+proto")
            req.body?.let { setBody(it) }
        }
        val bodyBytes = resp.readRawBytes()
        val reader = FrameReader2()
        val frames = reader.push(bodyBytes)
        var idx = 0
        return object : Stream {
            override suspend fun recv(): ByteArray? = if (idx < frames.size) frames[idx++] else null
            override fun cancel() {}
        }
    }
}

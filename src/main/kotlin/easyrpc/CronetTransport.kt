package easyrpc

import easyrpc.FrameReader
import easyrpc.RPCError
import easyrpc.decodeEndStream
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.suspendCancellableCoroutine
import org.chromium.net.CronetEngine
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import org.chromium.net.UploadDataProviders
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val READ_CHUNK = 64 * 1024

// Callback executor: a small shared pool. Cronet callbacks MUST NOT run
// synchronously on the engine thread (the Java fallback engine can deadlock
// re-entering its own lock) — the docs' direct-executor trick only applies
// to the native engine.
private val CALLBACK_EXECUTOR: Executor = Executors.newCachedThreadPool { r ->
    Thread(r, "easyrpc-cronet-cb").apply { isDaemon = true }
}

/**
 * Cronet transport (h1 + h2 + h3/QUIC at the Chromium network-stack level).
 *
 * The cronet API is a compile-only dependency: applications provide
 * `org.chromium.net:cronet-embedded` (JVM/desktop) or Play-Services Cronet
 * (Android) on their own classpath, keeping the core dependency-free.
 *
 * The engine is caller-provided: on Android build it with a Context
 * (`CronetEngine.Builder(context).enableHttp2(true).enableQuic(true)`); on
 * JVM bring your own embedded engine. Cronet neither reads the JVM
 * trustStore nor the (GUI-gated) system keychains — private CAs must be
 * trusted by the OS (Android: install into the system store) or via the
 * engine's own verifier configuration.
 *
 * @param base base URL prefix for relative request paths.
 * @param engine the caller-built Cronet engine.
 */
class CronetTransport(
    private val base: String = "",
    private val engine: CronetEngine,
) : Transport {

    private fun url(u: String): String =
        if (u.startsWith("http://") || u.startsWith("https://")) u else base + u

    override suspend fun send(req: Request): Response =
        suspendCancellableCoroutine { cont ->
            val out = ByteArrayOutputStream()
            val callback = object : UrlRequest.Callback() {
                override fun onRedirectReceived(r: UrlRequest, info: UrlResponseInfo, newLocationUrl: String) {
                    r.followRedirect()
                }

                override fun onResponseStarted(r: UrlRequest, info: UrlResponseInfo) {
                    r.read(ByteBuffer.allocateDirect(READ_CHUNK))
                }

                override fun onReadCompleted(r: UrlRequest, info: UrlResponseInfo, byteBuffer: ByteBuffer) {
                    byteBuffer.flip()
                    val chunk = ByteArray(byteBuffer.remaining())
                    byteBuffer.get(chunk)
                    synchronized(out) { out.write(chunk) }
                    byteBuffer.clear()
                    r.read(byteBuffer)
                }

                override fun onSucceeded(r: UrlRequest, info: UrlResponseInfo) {
                    val body = synchronized(out) { out.toByteArray() }
                    cont.resumeWith(Result.success(toResponse(info, body)))
                }

                override fun onFailed(r: UrlRequest, info: UrlResponseInfo?, error: CronetException) {
                    cont.resumeWith(Result.failure(RPCError(13, "cronet: ${error.message}")))
                }

                override fun onCanceled(r: UrlRequest, info: UrlResponseInfo?) {
                    if (cont.isActive) {
                        cont.resumeWith(Result.failure(RPCError(1, "canceled")))
                    }
                }
            }
            val request = newRequest(req, callback).apply { start() }
            cont.invokeOnCancellation { try { request.cancel() } catch (_: Exception) {} }
        }

    override suspend fun openStream(req: Request): Stream {
        // Incremental delivery: body chunks flow through a channel into the
        // shared FrameReader (END terminates, end-stream errors surface via
        // lastError(), finish() catches truncation — same semantics as every
        // other adapter).
        val channel = Channel<ByteArray>(Channel.UNLIMITED)
        // per-stream holder (concurrent streams must not clobber each other)
        val holder = HttpErrorHolder()
        val request = newRequest(req, object : UrlRequest.Callback() {
            override fun onRedirectReceived(r: UrlRequest, info: UrlResponseInfo, newLocationUrl: String) {
                r.followRedirect()
            }

            override fun onResponseStarted(r: UrlRequest, info: UrlResponseInfo) {
                r.read(ByteBuffer.allocateDirect(READ_CHUNK))
            }

            override fun onReadCompleted(r: UrlRequest, info: UrlResponseInfo, byteBuffer: ByteBuffer) {
                byteBuffer.flip()
                val chunk = ByteArray(byteBuffer.remaining())
                byteBuffer.get(chunk)
                byteBuffer.clear()
                channel.trySend(chunk)
                r.read(byteBuffer)
            }

            override fun onSucceeded(r: UrlRequest, info: UrlResponseInfo) {
                if (info.httpStatusCode >= 300) {
                    // HTTP-level error on a stream call: all chunks were
                    // already delivered to the channel — drain them into the
                    // shared error fallback chain.
                    val body = ByteArrayOutputStream()
                    while (true) {
                        val chunk = channel.tryReceive().getOrNull() ?: break
                        body.write(chunk)
                    }
                    holder.err = rpcErrorFrom(
                        info.httpStatusCode,
                        info.allHeaders.entries.associate { (k, v) -> k.lowercase() to v },
                        body.toByteArray(),
                    )
                }
                channel.close()
            }

            override fun onFailed(r: UrlRequest, info: UrlResponseInfo?, error: CronetException) {
                holder.err = RPCError(13, "cronet: ${error.message}")
                channel.close()
            }

            override fun onCanceled(r: UrlRequest, info: UrlResponseInfo?) {
                holder.err = RPCError(1, "canceled")
                channel.close()
            }
        }).apply { start() }

        return object : Stream {
            private val reader = FrameReader()
            private val pushed = ArrayDeque<ByteArray>()
            private var ended = false
            private var streamErr: RPCError? = null

            override suspend fun recv(): ByteArray? {
                if (pushed.isNotEmpty()) return pushed.removeFirst()
                if (ended) return null
                while (true) {
                    // Suspends until a chunk arrives or the channel CLOSES —
                    // a momentarily-empty channel must not be mistaken for a
                    // finished body (tryReceive would do exactly that).
                    val chunk = channel.receiveCatching().getOrNull()
                    if (chunk == null) {
                        // channel closed: source ended
                        ended = true
                        holder.err?.let { streamErr = it; return null }
                        try {
                            reader.finish()
                        } catch (e: RPCError) {
                            streamErr = e
                        }
                        return null
                    }
                    var frames = reader.push(chunk)
                    val endIdx = frames.indexOfFirst { it.end }
                    if (endIdx >= 0) {
                        val endFrame = frames[endIdx]
                        val (code, message, details) = decodeEndStream(endFrame.payload)
                        if (code != 0) streamErr = RPCError(code, message, details)
                        frames = frames.subList(0, endIdx)
                        frames.forEach { pushed.addLast(it.payload) }
                        ended = true
                        if (pushed.isNotEmpty()) return pushed.removeFirst()
                        return null
                    }
                    frames.forEach { pushed.addLast(it.payload) }
                    if (pushed.isNotEmpty()) return pushed.removeFirst()
                    // partial frame: keep reading
                }
            }

            override fun lastError(): RPCError? = streamErr

            override fun cancel() {
                try {
                    request.cancel()
                } catch (_: Exception) {
                }
            }
        }
    }

    private fun newRequest(req: Request, callback: UrlRequest.Callback): UrlRequest {
        val builder = engine.newUrlRequestBuilder(url(req.url), callback, CALLBACK_EXECUTOR)
            .setHttpMethod(req.method)
        // Caller-supplied headers first; default content-type ONLY when the
        // caller did not set one (the JSON codec must survive — same rule as
        // every other adapter).
        val hasCt = req.headers.keys.any { it.equals("content-type", ignoreCase = true) }
        if (!hasCt) builder.addHeader("content-type", "application/proto")
        for ((k, vs) in req.headers) {
            for (v in vs) builder.addHeader(k, v)
        }
        req.body?.let { builder.setUploadDataProvider(UploadDataProviders.create(it), CALLBACK_EXECUTOR) }
        return builder.build()
    }

    private fun toResponse(info: UrlResponseInfo, body: ByteArray): Response {
        val status = info.httpStatusCode
        val headers = info.allHeaders.entries.associate { (k, v) -> k.lowercase() to v }
        return Response(
            status,
            headers,
            body,
            if (status >= 300) rpcErrorFrom(status, headers, body) else null,
        )
    }

    private class HttpErrorHolder {
        var err: RPCError? = null
    }

}

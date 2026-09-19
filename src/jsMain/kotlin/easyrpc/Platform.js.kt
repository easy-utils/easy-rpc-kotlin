package easyrpc

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * JS gzip via WHATWG `CompressionStream`/`DecompressionStream`. Kotlin/JS
 * `@JsFun` cannot pass ByteArray, so bytes cross the boundary as base64
 * strings (padded standard alphabet; the core's error-detail base64 is a
 * separate, unpadded concern).
 */
actual suspend fun gzipCompress(data: ByteArray): ByteArray =
    jsTransform("gzip", data, compress = true, failOnError = false)

actual suspend fun gzipDecompress(data: ByteArray): ByteArray {
    if (data.isEmpty()) throw RPCError(13, "corrupt gzip frame: empty input")
    return jsTransform("gzip", data, compress = false, failOnError = true)
}

private suspend fun jsTransform(format: String, data: ByteArray, compress: Boolean, failOnError: Boolean): ByteArray =
    suspendCancellableCoroutine { cont ->
        val b64 = b64Full(data)
        val onOk: (String) -> Unit = { ok -> cont.resume(b64DecodeFull(ok)) }
        val onErr: (String) -> Unit = { e ->
            if (failOnError) cont.resumeWithException(RPCError(13, "corrupt gzip frame: $e"))
            else cont.resume(data) // gzipCompress is opportunistic: identity on failure
        }
        if (compress) jsCompress(format, b64, onOk, onErr) else jsDecompress(format, b64, onOk, onErr)
    }

// Full (padded) base64 for byte transport across the JS boundary.
private const val B64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
private fun b64Full(data: ByteArray): String {
    val out = StringBuilder((data.size + 2) / 3 * 4)
    var i = 0
    while (i < data.size) {
        val n = (data[i].toInt() and 0xff shl 16) or
            (if (i + 1 < data.size) data[i + 1].toInt() and 0xff shl 8 else 0) or
            (if (i + 2 < data.size) data[i + 2].toInt() and 0xff else 0)
        out.append(B64[n ushr 18 and 63]); out.append(B64[n ushr 12 and 63])
        out.append(if (i + 1 < data.size) B64[n ushr 6 and 63] else '=')
        out.append(if (i + 2 < data.size) B64[n and 63] else '='); i += 3
    }
    return out.toString()
}
private fun b64DecodeFull(s: String): ByteArray {
    val out = ArrayList<Byte>(s.length * 3 / 4); var buf = 0; var bits = 0
    for (c in s) {
        val d = when (c) {
            in 'A'..'Z' -> c - 'A'; in 'a'..'z' -> c - 'a' + 26; in '0'..'9' -> c - '0' + 52
            '+' -> 62; '/' -> 63; '=' -> break; else -> continue
        }
        buf = buf shl 6 or d; bits += 6
        if (bits >= 8) { bits -= 8; out.add((buf ushr bits).toByte()) }
    }
    return out.toByteArray()
}

@JsFun("(fmt, b64, onOk, onErr) => { try { const bin = atob(b64); const bytes = new Uint8Array(bin.length); for (let i=0;i<bin.length;i++) bytes[i]=bin.charCodeAt(i); const cs = new CompressionStream(fmt); const p = new Blob([bytes]).stream().pipeThrough(cs); new Response(p).arrayBuffer().then(b => { const u = new Uint8Array(b); let s=''; for (let i=0;i<u.length;i++) s+=String.fromCharCode(u[i]); onOk(btoa(s)); }).catch(e => onErr(String(e))); } catch(e) { onErr(String(e)); } }")
external fun jsCompress(format: String, b64: String, onOk: (String) -> Unit, onErr: (String) -> Unit)

@JsFun("(fmt, b64, onOk, onErr) => { try { const bin = atob(b64); const bytes = new Uint8Array(bin.length); for (let i=0;i<bin.length;i++) bytes[i]=bin.charCodeAt(i); const ds = new DecompressionStream(fmt); const p = new Blob([bytes]).stream().pipeThrough(ds); new Response(p).arrayBuffer().then(b => { const u = new Uint8Array(b); let s=''; for (let i=0;i<u.length;i++) s+=String.fromCharCode(u[i]); onOk(btoa(s)); }).catch(e => onErr(String(e))); } catch(e) { onErr(String(e)); } }")
external fun jsDecompress(format: String, b64: String, onOk: (String) -> Unit, onErr: (String) -> Unit)

actual fun defaultTransport(base: String): Transport = KtorTransport(HttpClient(Js), base)

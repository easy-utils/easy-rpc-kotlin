package easyrpc

import okio.Buffer
import okio.GzipSink
import okio.GzipSource
import okio.buffer
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

/** Native gzip via Okio (okio's zlib source set compiles for native targets). */
actual suspend fun gzipCompress(data: ByteArray): ByteArray {
    return try {
        val buffer = Buffer()
        val sink = GzipSink(buffer).buffer()
        sink.write(data)
        sink.close()
        buffer.readByteArray()
    } catch (e: Exception) {
        data
    }
}

actual suspend fun gzipDecompress(data: ByteArray): ByteArray {
    if (data.isEmpty()) throw RPCError(13, "corrupt gzip frame: empty input")
    return try {
        val buffer = Buffer().apply { write(data) }
        val source = GzipSource(buffer).buffer()
        val out = source.readByteArray()
        source.close()
        out
    } catch (e: Exception) {
        throw RPCError(13, "corrupt gzip frame: " + (e.message ?: e.toString()))
    }
}

/** Native platform default: Ktor CIO (HTTP/1.1, pure Kotlin). */
actual fun defaultTransport(base: String): Transport = KtorTransport(HttpClient(CIO), base)

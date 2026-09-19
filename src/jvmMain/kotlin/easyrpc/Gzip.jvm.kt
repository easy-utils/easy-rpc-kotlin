package easyrpc

import okio.Buffer
import okio.GzipSink
import okio.GzipSource
import okio.buffer

/** JVM gzip via Okio (synchronous under the hood, exposed as suspend by the
 *  common contract). */
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

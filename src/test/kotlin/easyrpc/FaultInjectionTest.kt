package easyrpc

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Fault injection (spec §4.2 M8/M10 + F2) at the protocol level: the frame
 * reader MUST error on truncated/END-less/corrupt-gzip bodies, reassemble
 * frames split across chunks, and treat a garbage END payload as a clean end.
 */
class FaultInjectionTest {
    private fun frame(payload: ByteArray, end: Boolean = false, compressed: Boolean = false): ByteArray {
        val flags = (if (end) 0x02 else 0) or (if (compressed) 0x01 else 0)
        val len = ByteArray(4)
        len[0] = (payload.size ushr 24).toByte(); len[1] = (payload.size ushr 16).toByte()
        len[2] = (payload.size ushr 8).toByte(); len[3] = payload.size.toByte()
        return byteArrayOf(flags.toByte()) + len + payload
    }

    private fun frames(vararg bufs: ByteArray, split: Int = Int.MAX_VALUE): List<Frame> {
        val r = FrameReader()
        val out = mutableListOf<Frame>()
        for (b in bufs) {
            var i = 0
            while (i < b.size) {
                val n = minOf(split, b.size - i)
                out += r.push(b.copyOfRange(i, i + n))
                i += n
            }
        }
        r.finish()
        return out
    }

    @Test fun f1_midFrameTruncationErrors() {
        val full = frame(ByteArray(8) { 1 })
        val cut = full.copyOf(full.size - 4)
        val r = FrameReader()
        val got = r.push(frame(byteArrayOf(0))) + r.push(cut)
        assertEquals(1, got.size)
        assertFailsWith<RPCError> { r.finish() }
    }

    @Test fun f2_bodyEndsWithoutEndFrameErrors() {
        val r = FrameReader()
        r.push(frame(byteArrayOf(0)))
        r.push(frame(byteArrayOf(1)))
        assertFailsWith<RPCError> { r.finish() }
    }

    @Test fun f3_garbageEndPayloadIsClean() {
        val got = frames(
            frame(byteArrayOf(0)),
            frame(byteArrayOf(0xff.toByte(), 0xfe.toByte(), 0x42), end = true),
        )
        val data = got.filter { !it.end }
        assertEquals(1, data.size)
        assertEquals(listOf(0.toByte()), data.map { it.payload[0] })
    }

    @Test fun f4_corruptGzipErrorsNeverRaw() {
        // valid gzip with a corrupted middle byte
        val gz = gzipCompress(byteArrayOf(1, 2, 3))
        gz[gz.size / 2] = (gz[gz.size / 2].toInt() xor 0xff).toByte()
        val r = FrameReader()
        assertFailsWith<RPCError> { r.push(frame(gz, compressed = true)) }
    }

    @Test fun f5_framesSplitAcrossChunksReassemble() {
        val body = frame(byteArrayOf(0)) + frame(byteArrayOf(1)) + frame(ByteArray(0), end = true)
        val got = FrameReader()
        val out = mutableListOf<Frame>()
        var i = 0
        while (i < body.size) {
            out += got.push(body.copyOfRange(i, minOf(i + 3, body.size)))
            i += 3
        }
        got.finish()
        assertEquals(listOf(0.toByte(), 1.toByte()), out.filter { !it.end }.map { f -> f.payload[0] })
    }

    @Test fun f6_validGzipDecodes() {
        val got = frames(
            frame(gzipCompress(byteArrayOf(7)), compressed = true),
            frame(ByteArray(0), end = true),
        )
        assertEquals(7.toByte(), got[0].payload[0])
    }
}

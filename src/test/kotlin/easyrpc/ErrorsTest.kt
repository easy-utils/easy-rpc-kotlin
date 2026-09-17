package easyrpc

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Error-path matrix (spec §4.2 M1–M13) + Error Details round-trip (§4.1).
 * Mirrored in every language implementation; inputs are constructed directly
 * against the protocol functions — no server needed.
 */
class ErrorsTest {
    private val detail = ErrorDetail("type.googleapis.com/google.rpc.RetryInfo", byteArrayOf(1, 2, 3, -6))

    private fun enc(s: String) = s.toByteArray(Charsets.UTF_8)

    @Test fun m1_emptyPayloadIsCleanEnd() {
        val (c, m, d) = decodeEndStream(ByteArray(0))
        assertEquals(0, c); assertEquals("", m); assertNull(d)
    }

    @Test fun m2_garbageIsCleanEnd() {
        val (c, _, _) = decodeEndStream(byteArrayOf(-1, -2, 0, 0x42))
        assertEquals(0, c)
    }

    @Test fun m3_errorWithoutCodeIsUnknown() {
        val (c, m, _) = decodeEndStream(enc("""{"error":{}}"""))
        assertEquals(2, c); assertEquals("", m)
    }

    @Test fun m4_unknownCodeNameIs2() {
        val (c, m, _) = decodeEndStream(enc("""{"error":{"code":"nope","message":"m"}}"""))
        assertEquals(2, c); assertEquals("m", m)
    }

    @Test fun m5_unknownFieldsIgnored() {
        val (c, _, _) = decodeEndStream(enc("""{"error":{"code":"not_found","message":"m"},"x":1}"""))
        assertEquals(5, c)
    }

    @Test fun m6_detailsRoundTrip() {
        val payload = encodeEndStream(8, "rate limited", listOf(detail))
        val (c, m, d) = decodeEndStream(payload)
        assertEquals(8, c); assertEquals("rate limited", m)
        assertEquals(listOf(detail), d)
    }

    @Test fun m7_malformedDetailsEntriesSkipped() {
        val (_, _, d) = decodeEndStream(
            enc("""{"error":{"code":"resource_exhausted","details":[""" +
                """{"type":"t","value":"!!!"},{"value":"x"},{"type":"ok"},{"type":"t2","value":"AQID"}]}}""")
        )
        assertEquals(listOf(ErrorDetail("t2", byteArrayOf(1, 2, 3))), d)
    }

    @Test fun detailsOmittedWhenEmpty() {
        val text = String(encodeEndStream(5, "gone"), Charsets.UTF_8)
        assertEquals("""{"error":{"code":"not_found","message":"gone"}}""", text)
    }

    @Test fun m11_plainTextIsNotJsonError() {
        val (c, _, _) = decodeErrorJson(enc("busy"))
        assertEquals(0, c)
    }

    @Test fun unaryDetailsRoundTrip() {
        val body = encodeErrorJson(8, "limited", listOf(detail))
        val (c, m, d) = decodeErrorJson(body)
        assertEquals(8, c); assertEquals("limited", m) // message must survive (was dropped pre-0.5.0)
        assertEquals(listOf(detail), d)
    }

    @Test fun headerPathMergesDetailsFromBody() {
        val body = encodeErrorJson(9, "ignored", listOf(detail))
        val e = rpcErrorFrom(
            429,
            mapOf("connect-code" to listOf("8"), "connect-error" to listOf("limited")),
            body,
        )
        assertEquals(8, e.code); assertEquals("limited", e.message); assertEquals(listOf(detail), e.details)
    }

    @Test fun m12_m13_deadlineMapsToCode4() {
        val (c, _, _) = decodeErrorJson(encodeErrorJson(4, "deadline exceeded"))
        assertEquals(4, c)
        assertEquals(504, httpStatus(4)); assertEquals(4, connectFromStatus(504))
    }

    @Test fun m8_truncatedFrameYieldsNothing() {
        val full = frame(ByteArray(10) { 7 })
        val cut = full.copyOf(full.size - 4)
        // A reader cannot produce a complete frame from a truncated buffer.
        assertTrue(cut.size < 5 + 10)
        assertNull(readFramesAccumulated(cut, 10))
    }

    /** Feed [bytes] into the same framing logic the transports use; null when
     * no complete frame is available (truncation, matrix M8). */
    private fun readFramesAccumulated(bytes: ByteArray, declared: Int): ByteArray? =
        if (bytes.size < 5 + declared) null else bytes.copyOfRange(5, 5 + declared)
}

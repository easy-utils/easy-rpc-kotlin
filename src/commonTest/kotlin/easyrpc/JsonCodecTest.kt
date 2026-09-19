package easyrpc

import com.easyrpc.conformance.v1.CountRequest
import com.easyrpc.conformance.v1.EchoBytesResponse
import com.easyrpc.conformance.v1.EchoResponse
import pbandk.ByteArr
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

// Transport-independent JSON codec tests (proto3 JSON via pbandk).
class JsonCodecTest {
    @Test fun contentKindMapping() {
        assertEquals(KIND_PROTO, contentKindOf("application/proto"))
        assertEquals(KIND_JSON, contentKindOf("application/json; charset=utf-8"))
        assertEquals(KIND_JSON, contentKindOf("application/connect+json"))
        assertNull(contentKindOf("text/plain"))
        assertEquals(CONTENT_TYPE_UNARY_JSON, contentTypeFor(false, KIND_JSON))
        assertEquals(CONTENT_TYPE_STREAM_JSON, contentTypeFor(true, KIND_JSON))
    }

    @Test fun jsonStringRoundTrip() {
        val m = EchoResponse(output = "echo:hi")
        val bytes = encodeMsg(m, KIND_JSON)
        val text = bytes.decodeToString()
        assertTrue(text.contains("output"), text)
        assertTrue(text.contains("echo:hi"), text)
        val back = decodeMsg(bytes, EchoResponse.Companion, KIND_JSON)
        assertEquals("echo:hi", back.output)
    }

    @Test fun jsonBytesAreBase64() {
        val m = EchoBytesResponse(data = ByteArr(byteArrayOf(0, 1, 2, 0xff.toByte(), 0xfe.toByte(), 0x80.toByte())))
        val bytes = encodeMsg(m, KIND_JSON)
        assertTrue(bytes.decodeToString().contains("AAEC"), bytes.decodeToString())
        val back = decodeMsg(bytes, EchoBytesResponse.Companion, KIND_JSON)
        assertTrue(m.data.array.contentEquals(back.data.array))
    }

    @Test fun jsonIgnoresUnknownFields() {
        val json = """{"count":42,"unknownField":"x"}""".encodeToByteArray()
        val m = decodeMsg(json, CountRequest.Companion, KIND_JSON)
        assertEquals(42, m.count)
    }
}

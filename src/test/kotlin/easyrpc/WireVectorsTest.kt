package easyrpc

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.io.File

/**
 * Wire golden-vector conformance (transport-independent): the protocol layer
 * must reproduce easy-rpc-spec/conformance/wire-vectors.json. Frames are
 * byte-exact; JSON payloads compare SEMANTICALLY (key order is not significant).
 */
class WireVectorsTest {
    private val v = parseJson(File("src/test/resources/wire-vectors.json").readText()) as Map<*, *>

    private fun hex(b: ByteArray) = b.joinToString("") { "%02x".format(it) }
    private fun unhex(s: String) = ByteArray(s.length / 2) { s.substring(it * 2, it * 2 + 2).toInt(16).toByte() }
    private fun arr(x: Any?) = (x as List<*>)

    @Test fun frames() {
        for (f in arr(v["frames"])) {
            val m = f as Map<*, *>
            val e = m["encode"] as Map<*, *>
            val raw = frame(unhex(e["payloadHex"] as String), e["end"] as Boolean).copyOf()
            if (e["compressed"] as Boolean) raw[0] = (raw[0].toInt() or 0x01).toByte()
            assertEquals(m["bytesHex"], hex(raw), m["name"] as String)
        }
    }

    @Test fun endStream() {
        for (e in arr(v["endStream"])) {
            val m = e as Map<*, *>
            val dec = m["decode"] as Map<*, *>
            val es = decodeEndStream(unhex(dec["bytesHex"] as String))
            assertEquals((m["code"] as Number).toInt(), es.code, "${m["name"]} code")
            assertEquals(m["message"], es.message, "${m["name"]} message")
            if (m["metadata"] != null) assertEquals(m["metadata"], es.metadata, "${m["name"]} metadata")
            val enc = m["encode"] as Map<*, *>?
            if (enc != null && m["bytesHex"] != null) {
                @Suppress("UNCHECKED_CAST")
                val md = (enc["metadata"] as? Map<String, List<String>>) ?: emptyMap()
                val got = encodeEndStream((enc["code"] as Number).toInt(), enc["message"] as String, null, md)
                assertEquals(parseJson(String(unhex(m["bytesHex"] as String))), parseJson(String(got)), "${m["name"]} encode")
            }
        }
    }

    @Test fun unaryError() {
        for (u in arr(v["unaryError"])) {
            val m = u as Map<*, *>
            val enc = m["encode"] as Map<*, *>
            @Suppress("UNCHECKED_CAST")
            val details = (enc["details"] as? List<Map<*, *>>)?.map {
                ErrorDetail(it["type"] as String, unhex(it["valueHex"] as String))
            }
            val got = encodeErrorJson((enc["code"] as Number).toInt(), enc["message"] as String, details)
            assertEquals(
                parseJson(String(unhex(m["bytesHex"] as String))),
                parseJson(String(got)),
                m["name"] as String,
            )
        }
    }

    @Test fun trailers() {
        for (t in arr(v["trailerHeaders"])) {
            val m = t as Map<*, *>
            val demux = m["demux"] as Map<*, *>?
            if (demux != null) {
                val (h, tl) = demuxTrailers(demux as Headers)
                assertEquals(m["headers"], h, "${m["name"]} headers")
                assertEquals(m["trailers"], tl, "${m["name"]} trailers")
            }
            val mux = m["mux"] as Map<*, *>?
            if (mux != null) {
                @Suppress("UNCHECKED_CAST")
                val got = muxTrailers(mux["headers"] as Headers, mux["trailers"] as Headers)
                assertEquals(m["result"], got, "${m["name"]} mux")
            }
        }
    }

    @Test fun codeMap() {
        for (c in arr(v["codeNames"])) {
            val m = c as Map<*, *>
            val code = (m["code"] as Number).toInt()
            assertEquals(m["name"], codeToString(code), "code $code")
            assertEquals(code, codeFromString(m["name"] as String), "name ${m["name"]}")
            if (code != 0) assertEquals((m["http"] as Number).toInt(), httpStatus(code), "http $code")
        }
    }
}

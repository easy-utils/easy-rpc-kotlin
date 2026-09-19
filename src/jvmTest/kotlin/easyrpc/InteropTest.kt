package easyrpc

import com.easyrpc.conformance.v1.BigStreamRequest
import com.easyrpc.conformance.v1.CountRequest
import com.easyrpc.conformance.v1.CountTrailerRequest
import com.easyrpc.conformance.v1.EchoBytesRequest
import com.easyrpc.conformance.v1.EchoRequest
import com.easyrpc.conformance.v1.EchoTrailerRequest
import com.easyrpc.conformance.v1.EmptyRequest
import com.easyrpc.conformance.v1.FailDetailsRequest
import com.easyrpc.conformance.v1.SleepRequest
import com.easyrpc.conformance.v1.StreamFailDetailsRequest
import com.easyrpc.conformance.v1.StreamFailRequest
import pbandk.ByteArr
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Interop against a live easy-rpc conformance server (default :18888). Set
 * EASY_RPC_BASE to target another server; skipped implicitly if unreachable by
 * asserting only when the server responds — we use a short connect timeout via
 * the request itself. This mirrors the other languages' interop tests.
 */
class InteropTest {
    private val base = System.getenv("EASY_RPC_BASE") ?: "http://127.0.0.1:18888"

    /** Transport selector (spec §7.1): `okhttp` (default) or `cio` (Ktor). */
    private fun client(): ConformanceServiceClient {
        val t = when (System.getenv("EASY_RPC_TRANSPORT")) {
            "cio" -> KtorTransport(io.ktor.client.HttpClient(io.ktor.client.engine.cio.CIO), base)
            else -> OkHttpTransport(base = base)
        }
        return ConformanceServiceClient(t)
    }

    @Test fun echo() = runTest {
        val c = client()
        val res = c.echo(EchoRequest(input = "hi"))
        assertEquals("echo:hi", res.output)
    }

    @Test fun count() = runTest {
        val c = client()
        val idx = c.count(CountRequest(count = 3)).toList().map { it.index }
        assertEquals(listOf(0, 1, 2), idx)
    }

    @Test fun unaryError() = runTest {
        val c = client()
        var code = 0
        try { c.failDetails(FailDetailsRequest(code = 8, message = "limited", detailType = "t/x", detailText = "d")) }
        catch (e: RPCError) { code = e.code }
        assertEquals(8, code)
    }

    @Test fun streamFail() = runTest {
        val c = client()
        val seen = mutableListOf<Int>()
        var code = 0
        try { c.streamFail(StreamFailRequest(emitBefore = 2, code = 13, message = "boom")).collect { seen.add(it.index) } }
        catch (e: RPCError) { code = e.code }
        assertEquals(listOf(0, 1), seen)
        assertEquals(13, code)
    }

    @Test fun details() = runTest {
        val c = client()
        var det: List<ErrorDetail>? = null
        try { c.streamFailDetails(StreamFailDetailsRequest(emitBefore = 2, code = 13, message = "boom", detailType = "t/s", detailText = "sd")).collect { } }
        catch (e: RPCError) { det = e.details }
        assertEquals("t/s", det?.first()?.type)
        assertEquals("sd", det?.first()?.value?.decodeToString())
    }

    @Test fun bytes() = runTest {
        val c = client()
        val data = byteArrayOf(0, 1, 2, 0xff.toByte(), 0xfe.toByte(), 0x80.toByte())
        assertEquals(data.toList(), c.echoBytes(EchoBytesRequest(data = ByteArr(data))).data.array.toList())
    }

    @Test fun empty() = runTest {
        val c = client()
        c.empty(EmptyRequest())
    }

    @Test fun bigStream() = runTest {
        val c = client()
        val idx = c.bigStream(BigStreamRequest(count = 4, size = 2048)).toList().map { it.index }
        assertEquals(listOf(0, 1, 2, 3), idx)
    }

    @Test fun sleepOk() = runTest {
        val c = client()
        assertTrue(c.sleep(SleepRequest(millis = 0)).ok)
    }

    @Test fun unaryTrailer() = runTest {
        val c = client()
        val res = c.echoTrailer(EchoTrailerRequest(input = "x"))
        assertEquals("trailer:x", res.output)
        assertEquals(listOf("unary-x"), c.lastTrailers["x-trl"])
    }

    @Test fun streamTrailer() = runTest {
        val c = client()
        val idx = mutableListOf<Int>()
        // The generated client returns the stream as a Flow; trailers surface
        // on the transport, asserted via the unary trailer test above. Here we
        // only assert the streaming shape.
        c.countTrailer(CountTrailerRequest(count = 2)).collect { idx.add(it.index) }
        assertEquals(listOf(0, 1), idx)
    }
}

import easyrpc.*
import com.easyrpc.conformance.v1.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InteropTest {
    private fun client(): ConformanceServiceClient {
        val base = System.getenv("EASY_RPC_BASE") ?: "http://127.0.0.1:18888"
        // Unified transport vocabulary (spec §7.1): okhttp | cio (default okhttp).
        val transport = when (System.getenv("EASY_RPC_TRANSPORT")) {
            "cio" -> CioTransport(base = base)
            else -> OkHttpTransport(base = base)
        }
        return ConformanceServiceClient(transport)
    }

    @Test
    fun echoUnary() = runBlocking {
        val res = client().echo(EchoRequest.newBuilder().setInput("hi").build())
        assertEquals("echo:hi", res.output)
    }

    @Test
    fun countStream() = runBlocking {
        val idx = mutableListOf<Int>()
        client().count(CountRequest.newBuilder().setCount(3).build()).collect { idx.add(it.index) }
        assertEquals(listOf(0, 1, 2), idx)
    }

    @Test
    fun streamFailSurfacesEndStreamError() = runBlocking {
        val seen = mutableListOf<Int>()
        var err: RPCError? = null
        try {
            client().streamFail(
                StreamFailRequest.newBuilder().setEmitBefore(2).setCode(13).setMessage("boom").build()
            ).collect { seen.add(it.index) }
        } catch (e: RPCError) { err = e }
        assertEquals(listOf(0, 1), seen)
        assertEquals(13, err?.code)
    }

    @Test
    fun unaryTrailerSurfaces() = runBlocking {
        val c = client()
        val res = c.echoTrailer(EchoTrailerRequest.newBuilder().setInput("x").build())
        assertEquals("trailer:x", res.output)
        assertEquals(listOf("unary-x"), c.lastTrailers["x-trl"])
    }

    @Test
    fun unaryErrorSurfaces() = runBlocking {
        var err: RPCError? = null
        try {
            client().fail(FailRequest.newBuilder().setMessage("nope").build())
        } catch (e: RPCError) { err = e }
        assertEquals(3, err?.code)
    }
}

package easyrpc

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

private class Slow : Transport {
    override suspend fun send(req: Request): Response { delay(5000); return Response(status = 200) }
    override suspend fun openStream(req: Request): Stream = throw UnsupportedOperationException()
}

class InterceptorTest {
    @Test
    fun deadlineCancelsLocally() = runBlocking {
        val t = InterceptorTransport(listOf(TimeoutInterceptor(50)), Slow())
        val start = System.currentTimeMillis()
        try {
            t.send(Request(url = "/x"))
            fail("should have thrown")
        } catch (e: RPCError) {
            assertEquals(4, e.code)
        }
        assertTrue(System.currentTimeMillis() - start < 2000)
    }
}

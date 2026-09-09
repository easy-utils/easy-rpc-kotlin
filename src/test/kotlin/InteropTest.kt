import easyrpc.*
import com.easyrpc.conformance.v1.EchoRequest
import com.easyrpc.conformance.v1.EchoResponse
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class InteropTest {
    @Test
    fun echoUnary() = runBlocking {
        val base = System.getenv("EASY_RPC_BASE") ?: "http://127.0.0.1:18888"
        val t: Transport = OkHttpTransport(base = base)
        val c = ConformanceServiceClient(t)
        val res = c.echo(EchoRequest.newBuilder().setInput("hi").build())
        assertEquals("echo:hi", res.output)
    }
}

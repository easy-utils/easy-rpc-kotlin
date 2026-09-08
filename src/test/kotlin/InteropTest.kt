import easyrpc.*
import com.easyrpc.conformance.v1.EchoRequest
import com.easyrpc.conformance.v1.EchoResponse
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class InteropTest {
    @Test
    fun echoUnary() = runBlocking {
        val t: Transport = OkHttpTransport()
        val req = Request(
            url = "http://127.0.0.1:18888/v1/echo",
            body = EchoRequest.newBuilder().setInput("hi").build().toByteArray(),
        )
        val res = t.send(req)
        assertEquals(200, res.status)
        val out = EchoResponse.parseFrom(res.body)
        assertEquals("echo:hi", out.output)
    }
}

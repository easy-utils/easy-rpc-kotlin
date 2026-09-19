package easyrpc

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** Multi-value headers. */
typealias Headers = Map<String, List<String>>

/** A structured error detail (spec §4.1, aligned with Connect Error Details /
 * gRPC google.rpc status details). [type] is a type URL; [value] is opaque
 * bytes (typically an encoded protobuf message). */
data class ErrorDetail(val type: String, val value: ByteArray) {
    override fun equals(other: Any?): Boolean =
        other is ErrorDetail && other.type == type && other.value.contentEquals(value)
    override fun hashCode(): Int = type.hashCode() * 31 + value.contentHashCode()
}

/** Wire error with a Connect code. */
data class RPCError(
    val code: Int,
    override val message: String,
    /** Optional structured details (spec §4.1); opaque to the wire layer. */
    val details: List<ErrorDetail>? = null,
) : Exception("easyrpc: code=$code $message")

/** Normalized request. */
data class Request(
    val url: String,
    val headers: Headers = emptyMap(),
    val body: ByteArray? = null,
    /** Local cancellation handle, platform-specific (e.g. a coroutine Job on
     *  JVM/native, or an AbortSignal on web). Adapters that support abort
     *  honour it. */
    val signal: CancelSignal? = null,
    /** Back-compat: a coroutine Job used by [TimeoutInterceptor]. */
    val job: kotlinx.coroutines.Job? = null,
)

/** Platform cancellation handle. Implementations are transport-specific. */
interface CancelSignal {
    fun cancel()
    val isCancelled: Boolean
}

/** Normalized response. */
data class Response(
    val status: Int,
    val headers: Headers = emptyMap(),
    val body: ByteArray = ByteArray(0),
    /** Unary trailing metadata (demuxed from `trailer-*` response headers). */
    val trailers: Headers = emptyMap(),
    val error: RPCError? = null,
)

/** Server-stream of raw message payloads. */
interface Stream {
    suspend fun recv(): ByteArray?   // null => end
    /** Set when the stream ended with a Connect end-stream error. */
    fun lastError(): RPCError? = null
    /** Trailing metadata from the END frame (available after the stream ends). */
    fun trailers(): Headers = emptyMap()
    fun cancel()
}

/** Core interface a bridge implements. */
interface Transport {
    suspend fun send(req: Request): Response
    suspend fun openStream(req: Request): Stream
}

fun httpStatus(code: Int): Int = when (code) {
    1 -> 499
    3 -> 400
    4 -> 504
    5 -> 404
    6 -> 409
    7 -> 403
    8 -> 429
    9 -> 400
    10 -> 409
    11 -> 400
    12 -> 501
    14 -> 503
    16 -> 401
    else -> 500
}

/** Connect code -> stable lowercase wire name. */
val CODE_NAMES = mapOf(
    0 to "ok", 1 to "canceled", 2 to "unknown", 3 to "invalid_argument",
    4 to "deadline_exceeded", 5 to "not_found", 6 to "already_exists",
    7 to "permission_denied", 8 to "resource_exhausted", 9 to "failed_precondition",
    10 to "aborted", 11 to "out_of_range", 12 to "unimplemented", 13 to "internal",
    14 to "unavailable", 15 to "data_loss", 16 to "unauthenticated",
)
fun codeToString(code: Int): String = CODE_NAMES[code] ?: "unknown"
fun codeFromString(name: String): Int = CODE_NAMES.entries.firstOrNull { it.value == name }?.key ?: 2

// ---- minimal JSON (dependency-free; details need real array/object parsing) ----

/** Parse a JSON document to Map<String,Any?>/List<Any?>/String/Long/Double/Boolean/null. */
internal fun parseJson(text: String): Any? = JsonParser(text).run { parseValue().also { skipWs() } }

private class JsonParser(private val s: String) {
    private var i = 0
    fun skipWs() { while (i < s.length && s[i].let { it == ' ' || it == '\n' || it == '\r' || it == '\t' }) i++ }
    fun parseValue(): Any? {
        skipWs()
        if (i >= s.length) return null
        return when (s[i]) {
            '{' -> parseObject()
            '[' -> parseArray()
            '"' -> parseString()
            't' -> { i += 4; true }
            'f' -> { i += 5; false }
            'n' -> { i += 4; null }
            else -> parseNumber()
        }
    }
    private fun parseObject(): MutableMap<String, Any?> {
        val out = LinkedHashMap<String, Any?>()
        i++ // {
        skipWs()
        if (i < s.length && s[i] == '}') { i++; return out }
        while (i < s.length) {
            skipWs()
            val k = parseString() as? String ?: break
            skipWs()
            if (i < s.length && s[i] == ':') i++
            out[k] = parseValue()
            skipWs()
            if (i < s.length && s[i] == ',') { i++; continue }
            break
        }
        if (i < s.length && s[i] == '}') i++
        return out
    }
    private fun parseArray(): MutableList<Any?> {
        val out = ArrayList<Any?>()
        i++ // [
        skipWs()
        if (i < s.length && s[i] == ']') { i++; return out }
        while (i < s.length) {
            out.add(parseValue())
            skipWs()
            if (i < s.length && s[i] == ',') { i++; continue }
            break
        }
        if (i < s.length && s[i] == ']') i++
        return out
    }
    private fun parseString(): String? {
        if (i >= s.length || s[i] != '"') return null
        i++
        val sb = StringBuilder()
        while (i < s.length) {
            when (val c = s[i]) {
                '"' -> { i++; return sb.toString() }
                '\\' -> {
                    i++
                    when (val e = if (i < s.length) s[i] else ' ') {
                        'u' -> {
                            if (i + 4 < s.length) {
                                sb.append(s.substring(i + 1, i + 5).toInt(16).toChar()); i += 4
                            }
                        }
                        'n' -> sb.append('\n'); 't' -> sb.append('\t'); 'r' -> sb.append('\r')
                        'b' -> sb.append('\b'); 'f' -> sb.append('\u000c')
                        else -> sb.append(e)
                    }
                    i++
                }
                else -> { sb.append(c); i++ }
            }
        }
        return sb.toString()
    }
    private fun parseNumber(): Any {
        val start = i
        if (i < s.length && (s[i] == '-' || s[i] == '+')) i++
        var isDouble = false
        while (i < s.length && (s[i].isDigit() || s[i] == '.' || s[i] == 'e' || s[i] == 'E' || s[i] == '-' || s[i] == '+')) {
            if (s[i] == '.' || s[i] == 'e' || s[i] == 'E') isDouble = true
            i++
        }
        val t = s.substring(start, i)
        return if (isDouble) t.toDouble() else (t.toLongOrNull() ?: 0L)
    }
}

private const val HEX = "0123456789abcdef"
private fun hex4(code: Int): String =
    buildString { append(HEX[(code shr 12) and 15]); append(HEX[(code shr 8) and 15]); append(HEX[(code shr 4) and 15]); append(HEX[code and 15]) }

/** JSON-escape a string (covers the chars that appear in codes/messages). */
internal fun jsonEscape(v: String): String = buildString {
    for (c in v) when (c) {
        '\\' -> append("\\\\")
        '"' -> append("\\\"")
        '\n' -> append("\\n"); '\r' -> append("\\r"); '\t' -> append("\\t")
        else -> if (c < ' ') append("\\u" + hex4(c.code)) else append(c)
    }
}

// ---- dependency-free base64 (works on every platform; unpadded standard) ----

private const val B64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

internal fun b64Encode(data: ByteArray): String {
    // UNPADDED standard base64 — matches Connect (base64.RawStdEncoding).
    val out = StringBuilder((data.size + 2) / 3 * 4)
    var i = 0
    while (i < data.size) {
        val n = (data[i].toInt() and 0xff shl 16) or
            (if (i + 1 < data.size) data[i + 1].toInt() and 0xff shl 8 else 0) or
            (if (i + 2 < data.size) data[i + 2].toInt() and 0xff else 0)
        out.append(B64[n ushr 18 and 63])
        out.append(B64[n ushr 12 and 63])
        if (i + 1 < data.size) out.append(B64[n ushr 6 and 63])
        if (i + 2 < data.size) out.append(B64[n and 63])
        i += 3
    }
    return out.toString()
}

internal fun b64Decode(v: String): ByteArray? {
    // Accept standard OR URL-safe base64, padded OR unpadded (Connect sends
    // unpadded). Length % 4 == 1 is always invalid.
    val s = v.trimEnd('=')
    if (s.length % 4 == 1) return null
    val out = ArrayList<Byte>(s.length * 3 / 4)
    var buf = 0
    var bits = 0
    for (c in s) {
        val d = when (c) {
            in 'A'..'Z' -> c - 'A'
            in 'a'..'z' -> c - 'a' + 26
            in '0'..'9' -> c - '0' + 52
            '+' , '-' -> 62
            '/', '_' -> 63
            else -> return null // strict: invalid char rejects the whole value (M7)
        }
        buf = buf shl 6 or d
        bits += 6
        if (bits >= 8) { bits -= 8; out.add((buf ushr bits).toByte()) }
    }
    return out.toByteArray()
}

private fun wireDetails(details: List<ErrorDetail>?): String =
    (details ?: emptyList()).joinToString(",", "[", "]") {
        "{\"type\":\"${jsonEscape(it.type)}\",\"value\":\"${b64Encode(it.value)}\"}"
    }

private fun parseWireDetails(v: Any?): List<ErrorDetail>? {
    if (v !is List<*>) return null
    val out = ArrayList<ErrorDetail>()
    for (el in v) {
        if (el !is Map<*, *>) continue
        val t = el["type"] as? String
        val value = el["value"] as? String
        if (t.isNullOrEmpty() || value.isNullOrEmpty()) continue
        b64Decode(value)?.let { out.add(ErrorDetail(t, it)) }
    }
    return if (out.isEmpty()) null else out
}

/** Encode a Connect end-stream payload; a clean end is empty. Details
 * (spec §4.1) are included when non-empty. */
fun encodeEndStream(
    code: Int,
    message: String,
    details: List<ErrorDetail>? = null,
    metadata: Headers = emptyMap(),
): ByteArray {
    val parts = mutableListOf<String>()
    if (code != 0) {
        var err = "\"error\":{\"code\":\"${codeToString(code)}\",\"message\":\"${jsonEscape(message)}\""
        if (!details.isNullOrEmpty()) err += ",\"details\":${wireDetails(details)}"
        parts.add(err + "}")
    }
    if (metadata.isNotEmpty()) {
        val entries = metadata.entries.filter { it.value.isNotEmpty() }.map { (k, v) ->
            val vals = v.joinToString(",") { "\"" + jsonEscape(it) + "\"" }
            "\"" + jsonEscape(k) + "\":[" + vals + "]"
        }
        if (entries.isNotEmpty()) parts.add("\"metadata\":{" + entries.joinToString(",") + "}")
    }
    val json = "{" + parts.joinToString(",") + "}"
    return json.encodeToByteArray()
}

/** Decode a Connect end-stream payload into (code, message, details);
 * (0, "", null) = clean end. Malformed input is a clean end (matrix M2); an
 * error object without a code maps to 2 (M3/M4); unknown fields ignored (M5). */
fun decodeEndStream(payload: ByteArray): EndStream {
    if (payload.isEmpty()) return EndStream(0, "", null, emptyMap())
    val root = parseJson(payload.decodeToString()) as? Map<*, *> ?: return EndStream(0, "", null, emptyMap())
    var metadata: Headers = emptyMap()
    (root["metadata"] as? Map<*, *>)?.let { md ->
        val m = mutableMapOf<String, List<String>>()
        for ((k, v) in md) {
            if (k is String && v is List<*>) {
                val vs = v.filterIsInstance<String>()
                if (vs.isNotEmpty()) m[k] = vs
            }
        }
        metadata = m
    }
    val err = root["error"] as? Map<*, *> ?: return EndStream(0, "", null, metadata)
    val name = err["code"] as? String
    return EndStream(
        name?.let { codeFromString(it) } ?: 2,
        (err["message"] as? String) ?: "",
        parseWireDetails(err["details"]),
        metadata,
    )
}

/** Decoded END frame: code/message/details + trailing metadata. */
data class EndStream(val code: Int, val message: String, val details: List<ErrorDetail>?, val metadata: Headers)

/** Split headers into (headers, trailers) by the `trailer-` prefix. */
fun demuxTrailers(all: Headers): Pair<Headers, Headers> {
    val h = mutableMapOf<String, List<String>>()
    val t = mutableMapOf<String, List<String>>()
    for ((k, v) in all) {
        if (k.startsWith("trailer-", ignoreCase = true)) t[k.substring(8).lowercase()] = v
        else h[k] = v
    }
    return h to t
}

/** Merge trailers into headers using the `trailer-` prefix. */
fun muxTrailers(headers: Headers, trailers: Headers): Headers {
    val out = headers.toMutableMap()
    for ((k, v) in trailers) out["trailer-${k.lowercase()}"] = v
    return out
}

/** Per-RPC context for generated handlers: request metadata + trailer channel. */
class HandlerContext(val headers: Headers = emptyMap()) {
    private val _trailers = mutableMapOf<String, List<String>>()
    fun setTrailer(key: String, value: String) {
        _trailers[key] = (_trailers[key] ?: emptyList()) + value
    }
    val trailers: Headers get() = _trailers
}

const val CONTENT_TYPE_UNARY = "application/proto"
const val CONTENT_TYPE_STREAM = "application/connect+proto"

/** Reconstruct the exact RPCError from the server's connect-code/connect-error
 *  headers (the HTTP status alone is lossy). */
fun rpcErrorFrom(status: Int, headers: Map<String, List<String>>, body: ByteArray): RPCError {
    val code = headers.entries.firstOrNull { it.key.equals("connect-code", ignoreCase = true) }?.value?.firstOrNull()
    val c = code?.toIntOrNull()
    if (c != null) {
        // The header carries the exact code; the JSON body (when present) may
        // still carry details - merge them (details never travel in headers).
        val msg = headers.entries.firstOrNull { it.key.equals("connect-error", ignoreCase = true) }?.value?.firstOrNull() ?: ""
        val (_, _, hd) = decodeErrorJson(body)
        return RPCError(c, msg, hd)
    }
    val (jc, jm, jd) = decodeErrorJson(body)
    if (jc != 0) return RPCError(jc, jm, jd)
    return RPCError(connectFromStatus(status), body.decodeToString())
}

const val HEADER_TIMEOUT = "connect-timeout-ms"
const val HEADER_PROTOCOL_VERSION = "connect-protocol-version"
const val HEADER_ACCEPT_ENCODING = "connect-accept-encoding"
const val ENCODING_GZIP = "gzip"
const val COMPRESS_MIN_BYTES = 1024
const val CONNECT_PROTOCOL_VERSION = "1"
const val DEFAULT_MAX_MESSAGE_BYTES = 4 * 1024 * 1024

/** Parse the Connect timeout header into milliseconds (0 = none). */
fun parseTimeout(value: String?): Int {
    val n = value?.toIntOrNull() ?: return 0
    return if (n > 0) n else 0
}

/** Attach a deadline to a request. */
fun withTimeout(req: Request, timeoutMs: Int): Request {
    if (timeoutMs <= 0) return req
    return req.copy(headers = req.headers + (HEADER_TIMEOUT to listOf(timeoutMs.toString())))
}

/** Connect unary error body `{code,message[,details]}`. */
fun encodeErrorJson(code: Int, message: String, details: List<ErrorDetail>? = null): ByteArray {
    var body = "{\"code\":\"${codeToString(code)}\",\"message\":\"${jsonEscape(message)}\""
    if (!details.isNullOrEmpty()) body += ",\"details\":${wireDetails(details)}"
    return (body + "}").encodeToByteArray()
}

/** Parse a Connect unary error body; (0, "", null) when not an error body. */
fun decodeErrorJson(body: ByteArray): Triple<Int, String, List<ErrorDetail>?> {
    if (body.isEmpty()) return Triple(0, "", null)
    val root = parseJson(body.decodeToString()) as? Map<*, *> ?: return Triple(0, "", null)
    val name = root["code"] as? String ?: return Triple(0, "", null)
    return Triple(codeFromString(name), (root["message"] as? String) ?: "", parseWireDetails(root["details"]))
}

fun connectFromStatus(status: Int): Int = when (status) {
    400 -> 3
    404 -> 5
    403 -> 7
    401 -> 16
    429 -> 8
    503 -> 14
    409 -> 10
    504 -> 4
    501 -> 12
    499 -> 1
    else -> 13
}

const val FLAG_END_STREAM: Int = 0x02
const val FLAG_COMPRESSED: Int = 0x01

/** Encode one streaming frame. */
fun frame(payload: ByteArray, end: Boolean = false): ByteArray {
    val out = ByteArray(5 + payload.size)
    out[0] = if (end) FLAG_END_STREAM.toByte() else 0
    out[1] = (payload.size ushr 24).toByte()
    out[2] = (payload.size ushr 16).toByte()
    out[3] = (payload.size ushr 8).toByte()
    out[4] = payload.size.toByte()
    payload.copyInto(out, 5)
    return out
}

/** Encode one streaming frame with the compressed flag set (caller gzipped). */
fun frameCompressed(gzipped: ByteArray, end: Boolean = false): ByteArray {
    val out = frame(gzipped, end)
    out[0] = (out[0].toInt() or FLAG_COMPRESSED).toByte()
    return out
}

/** One decoded frame: payload + whether it is the END frame. When [compressed]
 * is true the payload is still gzip-compressed; the caller must decompress it
 * (gzip is async on web, so the reader stays pure + synchronous). */
data class Frame(val payload: ByteArray, val end: Boolean, val compressed: Boolean = false)

/**
 * De-frames a byte stream into typed frames. Pure and synchronous: it never
 * decompresses, it only reports [Frame.compressed]. Callers (transports) run
 * the payload through [gzipDecompress] when flagged.
 */
class FrameReader {
    private var acc = ByteArray(0)
    private var sawEnd = false

    fun push(buf: ByteArray): List<Frame> {
        acc += buf
        val out = mutableListOf<Frame>()
        while (true) {
            if (acc.size < 5) break
            val flags = acc[0].toInt() and 0xff
            val len = ((acc[1].toInt() and 0xff) shl 24) or
                ((acc[2].toInt() and 0xff) shl 16) or
                ((acc[3].toInt() and 0xff) shl 8) or
                (acc[4].toInt() and 0xff)
            if (acc.size < 5 + len) break
            val payload = acc.copyOfRange(5, 5 + len)
            acc = acc.copyOfRange(5 + len, acc.size)
            val end = (flags and FLAG_END_STREAM) != 0
            if (end) sawEnd = true
            out.add(Frame(payload, end, (flags and FLAG_COMPRESSED) != 0))
        }
        return out
    }

    /** Must be called when the source ends. Fault matrix F2/M8: the Connect
     * protocol requires every server-stream to terminate with an END frame; a
     * body that ends without one (or with trailing partial bytes) was
     * truncated mid-stream. */
    fun finish() {
        if (acc.isNotEmpty()) throw RPCError(13, "truncated frame at end of stream")
        if (!sawEnd) throw RPCError(13, "stream ended without END frame")
    }
}

/** Decode a single frame body (a [Frame]) into a data payload + optional
 *  END-frame error/trailers. Decompresses when flagged. */
suspend fun decodeFrame(f: Frame): DecodedFrame {
    val payload = if (f.compressed) gzipDecompress(f.payload) else f.payload
    if (!f.end) return DecodedFrame(payload, null, emptyMap())
    val es = decodeEndStream(payload)
    return DecodedFrame(
        body = ByteArray(0),
        error = if (es.code != 0) RPCError(es.code, es.message, es.details) else null,
        trailers = es.metadata,
    )
}

/** Result of [decodeFrame]: a data payload (non-END) or an END frame's
 * error/metadata. */
data class DecodedFrame(val body: ByteArray, val error: RPCError?, val trailers: Headers)

/** A call interceptor: mutate the request (auth/metadata), impose a deadline,
 *  observe, or short-circuit. `next_` performs the call. */
interface Interceptor {
    suspend fun unary(req: Request, next_: suspend (Request) -> Response): Response = next_(req)
    suspend fun stream(req: Request, next_: suspend (Request) -> Stream): Stream = next_(req)
}

/** Apply interceptors (first = outermost) around a Transport. */
class InterceptorTransport(private val ics: List<Interceptor>, private val inner: Transport) : Transport {
    override suspend fun send(req: Request): Response {
        suspend fun dispatch(i: Int, r: Request): Response =
            if (i >= ics.size) inner.send(r) else ics[i].unary(r) { nr -> dispatch(i + 1, nr) }
        return dispatch(0, req)
    }
    override suspend fun openStream(req: Request): Stream {
        suspend fun dispatch(i: Int, r: Request): Stream =
            if (i >= ics.size) inner.openStream(r) else ics[i].stream(r) { nr -> dispatch(i + 1, nr) }
        return dispatch(0, req)
    }
}

/** Attach fixed metadata to every call. */
class MetadataInterceptor(private val md: Map<String, List<String>>) : Interceptor {
    private fun aug(req: Request): Request {
        val h = req.headers.toMutableMap()
        for ((k, v) in md) if (!h.containsKey(k)) h[k] = v
        return req.copy(headers = h)
    }
    override suspend fun unary(req: Request, next_: suspend (Request) -> Response) = next_(aug(req))
    override suspend fun stream(req: Request, next_: suspend (Request) -> Stream) = next_(aug(req))
}

/** Attach a Connect deadline to every call; enforces locally via withTimeout
 *  so it works over any adapter. */
class TimeoutInterceptor(private val ms: Int) : Interceptor {
    private suspend fun <T> run(req: Request, next_: suspend (Request) -> T): T {
        if (ms <= 0) return next_(req)
        return try {
            kotlinx.coroutines.withTimeout(ms.toLong()) {
                val job = kotlinx.coroutines.currentCoroutineContext()[kotlinx.coroutines.Job]
                next_(withTimeout(req.copy(job = job), ms))
            }
        } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
            throw RPCError(4, "deadline exceeded")
        }
    }
    override suspend fun unary(req: Request, next_: suspend (Request) -> Response) = run(req, next_)
    override suspend fun stream(req: Request, next_: suspend (Request) -> Stream) = run(req, next_)
}

/** Adapter modes for the Kotlin composition root. */
enum class TransportMode { AUTO }

/**
 * Composition root: build the platform-default [Transport], install the
 * built-in metadata/deadline interceptors, then any [extra]. Swapping the
 * platform transport leaves the interceptors unchanged.
 */
fun connect(
    baseUrl: String,
    token: String = "",
    timeoutMs: Int = 0,
    extra: List<Interceptor> = emptyList(),
): Transport {
    val inner: Transport = defaultTransport(baseUrl)
    val ics = buildList {
        if (token.isNotEmpty()) add(MetadataInterceptor(mapOf("Authorization" to listOf("Bearer $token"))))
        if (timeoutMs > 0) add(TimeoutInterceptor(timeoutMs))
        addAll(extra)
    }
    return if (ics.isEmpty()) inner else InterceptorTransport(ics, inner)
}

/** Platform-default transport (JVM: OkHttp; native: Ktor CIO; web: Ktor fetch). */
expect fun defaultTransport(base: String): Transport

/** gzip-compress. On JVM/native this is synchronous; on web it uses the async
 *  WHATWG CompressionStream, so the signature is `suspend` everywhere. */
expect suspend fun gzipCompress(data: ByteArray): ByteArray

/** gzip-decompress. THROWS RPCError(13) on corrupt input (fault matrix M10):
 * a flagged-but-corrupt gzip payload is a protocol error, never raw compressed
 * bytes. */
expect suspend fun gzipDecompress(data: ByteArray): ByteArray

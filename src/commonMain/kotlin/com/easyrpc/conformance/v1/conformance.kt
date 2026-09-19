@file:OptIn(pbandk.PublicForGeneratedCode::class)

package com.easyrpc.conformance.v1

@pbandk.Export
public data class EchoRequest(
    val input: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.EchoRequest by lazy { com.easyrpc.conformance.v1.EchoRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoRequest = com.easyrpc.conformance.v1.EchoRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EchoRequest",
            messageClass = com.easyrpc.conformance.v1.EchoRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "input",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "input",
                        value = com.easyrpc.conformance.v1.EchoRequest::input
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EchoResponse(
    val output: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.EchoResponse by lazy { com.easyrpc.conformance.v1.EchoResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoResponse = com.easyrpc.conformance.v1.EchoResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EchoResponse",
            messageClass = com.easyrpc.conformance.v1.EchoResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "output",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "output",
                        value = com.easyrpc.conformance.v1.EchoResponse::output
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CountRequest(
    val count: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.CountRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.CountRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.CountRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.CountRequest by lazy { com.easyrpc.conformance.v1.CountRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.CountRequest = com.easyrpc.conformance.v1.CountRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.CountRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.CountRequest",
            messageClass = com.easyrpc.conformance.v1.CountRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "count",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "count",
                        value = com.easyrpc.conformance.v1.CountRequest::count
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CountResponse(
    val index: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.CountResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.CountResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.CountResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.CountResponse by lazy { com.easyrpc.conformance.v1.CountResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.CountResponse = com.easyrpc.conformance.v1.CountResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.CountResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.CountResponse",
            messageClass = com.easyrpc.conformance.v1.CountResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "index",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "index",
                        value = com.easyrpc.conformance.v1.CountResponse::index
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class FailRequest(
    val message: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.FailRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.FailRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.FailRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.FailRequest by lazy { com.easyrpc.conformance.v1.FailRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.FailRequest = com.easyrpc.conformance.v1.FailRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.FailRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.FailRequest",
            messageClass = com.easyrpc.conformance.v1.FailRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "message",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "message",
                        value = com.easyrpc.conformance.v1.FailRequest::message
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class FailResponse(
    val ok: Boolean = false,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.FailResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.FailResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.FailResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.FailResponse by lazy { com.easyrpc.conformance.v1.FailResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.FailResponse = com.easyrpc.conformance.v1.FailResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.FailResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.FailResponse",
            messageClass = com.easyrpc.conformance.v1.FailResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ok",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "ok",
                        value = com.easyrpc.conformance.v1.FailResponse::ok
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class HealthRequest(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.HealthRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.HealthRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.HealthRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.HealthRequest by lazy { com.easyrpc.conformance.v1.HealthRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.HealthRequest = com.easyrpc.conformance.v1.HealthRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.HealthRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.HealthRequest",
            messageClass = com.easyrpc.conformance.v1.HealthRequest::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class HealthResponse(
    val ok: Boolean = false,
    val name: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.HealthResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.HealthResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.HealthResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.HealthResponse by lazy { com.easyrpc.conformance.v1.HealthResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.HealthResponse = com.easyrpc.conformance.v1.HealthResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.HealthResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.HealthResponse",
            messageClass = com.easyrpc.conformance.v1.HealthResponse::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ok",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "ok",
                        value = com.easyrpc.conformance.v1.HealthResponse::ok
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "name",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "name",
                        value = com.easyrpc.conformance.v1.HealthResponse::name
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EchoBytesRequest(
    val data: pbandk.ByteArr = pbandk.ByteArr.empty,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoBytesRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoBytesRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoBytesRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.EchoBytesRequest by lazy { com.easyrpc.conformance.v1.EchoBytesRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoBytesRequest = com.easyrpc.conformance.v1.EchoBytesRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoBytesRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EchoBytesRequest",
            messageClass = com.easyrpc.conformance.v1.EchoBytesRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "data",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bytes(),
                        jsonName = "data",
                        value = com.easyrpc.conformance.v1.EchoBytesRequest::data
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EchoBytesResponse(
    val data: pbandk.ByteArr = pbandk.ByteArr.empty,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoBytesResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoBytesResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoBytesResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.EchoBytesResponse by lazy { com.easyrpc.conformance.v1.EchoBytesResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoBytesResponse = com.easyrpc.conformance.v1.EchoBytesResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoBytesResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EchoBytesResponse",
            messageClass = com.easyrpc.conformance.v1.EchoBytesResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "data",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bytes(),
                        jsonName = "data",
                        value = com.easyrpc.conformance.v1.EchoBytesResponse::data
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class SleepRequest(
    val millis: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.SleepRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.SleepRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.SleepRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.SleepRequest by lazy { com.easyrpc.conformance.v1.SleepRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.SleepRequest = com.easyrpc.conformance.v1.SleepRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.SleepRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.SleepRequest",
            messageClass = com.easyrpc.conformance.v1.SleepRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "millis",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "millis",
                        value = com.easyrpc.conformance.v1.SleepRequest::millis
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class SleepResponse(
    val ok: Boolean = false,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.SleepResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.SleepResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.SleepResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.SleepResponse by lazy { com.easyrpc.conformance.v1.SleepResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.SleepResponse = com.easyrpc.conformance.v1.SleepResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.SleepResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.SleepResponse",
            messageClass = com.easyrpc.conformance.v1.SleepResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ok",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "ok",
                        value = com.easyrpc.conformance.v1.SleepResponse::ok
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EmptyRequest(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EmptyRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EmptyRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EmptyRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.EmptyRequest by lazy { com.easyrpc.conformance.v1.EmptyRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EmptyRequest = com.easyrpc.conformance.v1.EmptyRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EmptyRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EmptyRequest",
            messageClass = com.easyrpc.conformance.v1.EmptyRequest::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class EmptyResponse(
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EmptyResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EmptyResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EmptyResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.EmptyResponse by lazy { com.easyrpc.conformance.v1.EmptyResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EmptyResponse = com.easyrpc.conformance.v1.EmptyResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EmptyResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EmptyResponse",
            messageClass = com.easyrpc.conformance.v1.EmptyResponse::class,
            messageCompanion = this,
            fields = buildList(0) {
            }
        )
    }
}

@pbandk.Export
public data class BigStreamRequest(
    val count: Int = 0,
    val size: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.BigStreamRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.BigStreamRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.BigStreamRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.BigStreamRequest by lazy { com.easyrpc.conformance.v1.BigStreamRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.BigStreamRequest = com.easyrpc.conformance.v1.BigStreamRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.BigStreamRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.BigStreamRequest",
            messageClass = com.easyrpc.conformance.v1.BigStreamRequest::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "count",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "count",
                        value = com.easyrpc.conformance.v1.BigStreamRequest::count
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "size",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "size",
                        value = com.easyrpc.conformance.v1.BigStreamRequest::size
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class BigStreamResponse(
    val index: Int = 0,
    val size: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.BigStreamResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.BigStreamResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.BigStreamResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.BigStreamResponse by lazy { com.easyrpc.conformance.v1.BigStreamResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.BigStreamResponse = com.easyrpc.conformance.v1.BigStreamResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.BigStreamResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.BigStreamResponse",
            messageClass = com.easyrpc.conformance.v1.BigStreamResponse::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "index",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "index",
                        value = com.easyrpc.conformance.v1.BigStreamResponse::index
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "size",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "size",
                        value = com.easyrpc.conformance.v1.BigStreamResponse::size
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class StreamFailRequest(
    val emitBefore: Int = 0,
    val code: Int = 0,
    val message: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.StreamFailRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.StreamFailRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.StreamFailRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.StreamFailRequest by lazy { com.easyrpc.conformance.v1.StreamFailRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.StreamFailRequest = com.easyrpc.conformance.v1.StreamFailRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.StreamFailRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.StreamFailRequest",
            messageClass = com.easyrpc.conformance.v1.StreamFailRequest::class,
            messageCompanion = this,
            fields = buildList(3) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "emit_before",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "emitBefore",
                        value = com.easyrpc.conformance.v1.StreamFailRequest::emitBefore
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "code",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "code",
                        value = com.easyrpc.conformance.v1.StreamFailRequest::code
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "message",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "message",
                        value = com.easyrpc.conformance.v1.StreamFailRequest::message
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class StreamFailResponse(
    val index: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.StreamFailResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.StreamFailResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.StreamFailResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.StreamFailResponse by lazy { com.easyrpc.conformance.v1.StreamFailResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.StreamFailResponse = com.easyrpc.conformance.v1.StreamFailResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.StreamFailResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.StreamFailResponse",
            messageClass = com.easyrpc.conformance.v1.StreamFailResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "index",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "index",
                        value = com.easyrpc.conformance.v1.StreamFailResponse::index
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EchoMetaRequest(
    val input: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoMetaRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoMetaRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoMetaRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.EchoMetaRequest by lazy { com.easyrpc.conformance.v1.EchoMetaRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoMetaRequest = com.easyrpc.conformance.v1.EchoMetaRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoMetaRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EchoMetaRequest",
            messageClass = com.easyrpc.conformance.v1.EchoMetaRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "input",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "input",
                        value = com.easyrpc.conformance.v1.EchoMetaRequest::input
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EchoMetaResponse(
    val input: String = "",
    val meta: Map<String, String> = emptyMap(),
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoMetaResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoMetaResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoMetaResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.EchoMetaResponse by lazy { com.easyrpc.conformance.v1.EchoMetaResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoMetaResponse = com.easyrpc.conformance.v1.EchoMetaResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoMetaResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EchoMetaResponse",
            messageClass = com.easyrpc.conformance.v1.EchoMetaResponse::class,
            messageCompanion = this,
            fields = buildList(2) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "input",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "input",
                        value = com.easyrpc.conformance.v1.EchoMetaResponse::input
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "meta",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Map<String, String>(keyType = pbandk.FieldDescriptor.Type.Primitive.String(), valueType = pbandk.FieldDescriptor.Type.Primitive.String()),
                        jsonName = "meta",
                        value = com.easyrpc.conformance.v1.EchoMetaResponse::meta
                    )
                )
            }
        )
    }

    public data class MetaEntry(
        override val key: String = "",
        override val value: String = "",
        override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
    ) : pbandk.Message, Map.Entry<String, String> {
        override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry = protoMergeImpl(other)
        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry> get() = Companion.descriptor
        override val protoSize: Int by lazy { super.protoSize }
        public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry> {
            public val defaultInstance: com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry by lazy { com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry() }
            override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry = com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry.decodeWithImpl(u)

            override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry> = pbandk.MessageDescriptor(
                fullName = "easyrpc.conformance.v1.EchoMetaResponse.MetaEntry",
                messageClass = com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry::class,
                messageCompanion = this,
                fields = buildList(2) {
                    add(
                        pbandk.FieldDescriptor(
                            messageDescriptor = this@Companion::descriptor,
                            name = "key",
                            number = 1,
                            type = pbandk.FieldDescriptor.Type.Primitive.String(),
                            jsonName = "key",
                            value = com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry::key
                        )
                    )
                    add(
                        pbandk.FieldDescriptor(
                            messageDescriptor = this@Companion::descriptor,
                            name = "value",
                            number = 2,
                            type = pbandk.FieldDescriptor.Type.Primitive.String(),
                            jsonName = "value",
                            value = com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry::value
                        )
                    )
                }
            )
        }
    }
}

@pbandk.Export
public data class BigRequest(
    val size: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.BigRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.BigRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.BigRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.BigRequest by lazy { com.easyrpc.conformance.v1.BigRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.BigRequest = com.easyrpc.conformance.v1.BigRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.BigRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.BigRequest",
            messageClass = com.easyrpc.conformance.v1.BigRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "size",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "size",
                        value = com.easyrpc.conformance.v1.BigRequest::size
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class BigResponse(
    val size: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.BigResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.BigResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.BigResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.BigResponse by lazy { com.easyrpc.conformance.v1.BigResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.BigResponse = com.easyrpc.conformance.v1.BigResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.BigResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.BigResponse",
            messageClass = com.easyrpc.conformance.v1.BigResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "size",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "size",
                        value = com.easyrpc.conformance.v1.BigResponse::size
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class FailDetailsRequest(
    val code: Int = 0,
    val message: String = "",
    val detailType: String = "",
    val detailText: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.FailDetailsRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.FailDetailsRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.FailDetailsRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.FailDetailsRequest by lazy { com.easyrpc.conformance.v1.FailDetailsRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.FailDetailsRequest = com.easyrpc.conformance.v1.FailDetailsRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.FailDetailsRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.FailDetailsRequest",
            messageClass = com.easyrpc.conformance.v1.FailDetailsRequest::class,
            messageCompanion = this,
            fields = buildList(4) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "code",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "code",
                        value = com.easyrpc.conformance.v1.FailDetailsRequest::code
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "message",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "message",
                        value = com.easyrpc.conformance.v1.FailDetailsRequest::message
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "detail_type",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "detailType",
                        value = com.easyrpc.conformance.v1.FailDetailsRequest::detailType
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "detail_text",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "detailText",
                        value = com.easyrpc.conformance.v1.FailDetailsRequest::detailText
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class FailDetailsResponse(
    val ok: Boolean = false,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.FailDetailsResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.FailDetailsResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.FailDetailsResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.FailDetailsResponse by lazy { com.easyrpc.conformance.v1.FailDetailsResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.FailDetailsResponse = com.easyrpc.conformance.v1.FailDetailsResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.FailDetailsResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.FailDetailsResponse",
            messageClass = com.easyrpc.conformance.v1.FailDetailsResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "ok",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Bool(),
                        jsonName = "ok",
                        value = com.easyrpc.conformance.v1.FailDetailsResponse::ok
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class StreamFailDetailsRequest(
    val emitBefore: Int = 0,
    val code: Int = 0,
    val message: String = "",
    val detailType: String = "",
    val detailText: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.StreamFailDetailsRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.StreamFailDetailsRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.StreamFailDetailsRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.StreamFailDetailsRequest by lazy { com.easyrpc.conformance.v1.StreamFailDetailsRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.StreamFailDetailsRequest = com.easyrpc.conformance.v1.StreamFailDetailsRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.StreamFailDetailsRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.StreamFailDetailsRequest",
            messageClass = com.easyrpc.conformance.v1.StreamFailDetailsRequest::class,
            messageCompanion = this,
            fields = buildList(5) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "emit_before",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "emitBefore",
                        value = com.easyrpc.conformance.v1.StreamFailDetailsRequest::emitBefore
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "code",
                        number = 2,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "code",
                        value = com.easyrpc.conformance.v1.StreamFailDetailsRequest::code
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "message",
                        number = 3,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "message",
                        value = com.easyrpc.conformance.v1.StreamFailDetailsRequest::message
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "detail_type",
                        number = 4,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "detailType",
                        value = com.easyrpc.conformance.v1.StreamFailDetailsRequest::detailType
                    )
                )
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "detail_text",
                        number = 5,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "detailText",
                        value = com.easyrpc.conformance.v1.StreamFailDetailsRequest::detailText
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class StreamFailDetailsResponse(
    val index: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.StreamFailDetailsResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.StreamFailDetailsResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.StreamFailDetailsResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.StreamFailDetailsResponse by lazy { com.easyrpc.conformance.v1.StreamFailDetailsResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.StreamFailDetailsResponse = com.easyrpc.conformance.v1.StreamFailDetailsResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.StreamFailDetailsResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.StreamFailDetailsResponse",
            messageClass = com.easyrpc.conformance.v1.StreamFailDetailsResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "index",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "index",
                        value = com.easyrpc.conformance.v1.StreamFailDetailsResponse::index
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EchoTrailerRequest(
    val input: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoTrailerRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoTrailerRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoTrailerRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.EchoTrailerRequest by lazy { com.easyrpc.conformance.v1.EchoTrailerRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoTrailerRequest = com.easyrpc.conformance.v1.EchoTrailerRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoTrailerRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EchoTrailerRequest",
            messageClass = com.easyrpc.conformance.v1.EchoTrailerRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "input",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "input",
                        value = com.easyrpc.conformance.v1.EchoTrailerRequest::input
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class EchoTrailerResponse(
    val output: String = "",
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.EchoTrailerResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoTrailerResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.EchoTrailerResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.EchoTrailerResponse by lazy { com.easyrpc.conformance.v1.EchoTrailerResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.EchoTrailerResponse = com.easyrpc.conformance.v1.EchoTrailerResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.EchoTrailerResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.EchoTrailerResponse",
            messageClass = com.easyrpc.conformance.v1.EchoTrailerResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "output",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.String(),
                        jsonName = "output",
                        value = com.easyrpc.conformance.v1.EchoTrailerResponse::output
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CountTrailerRequest(
    val count: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.CountTrailerRequest = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.CountTrailerRequest> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.CountTrailerRequest> {
        public val defaultInstance: com.easyrpc.conformance.v1.CountTrailerRequest by lazy { com.easyrpc.conformance.v1.CountTrailerRequest() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.CountTrailerRequest = com.easyrpc.conformance.v1.CountTrailerRequest.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.CountTrailerRequest> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.CountTrailerRequest",
            messageClass = com.easyrpc.conformance.v1.CountTrailerRequest::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "count",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "count",
                        value = com.easyrpc.conformance.v1.CountTrailerRequest::count
                    )
                )
            }
        )
    }
}

@pbandk.Export
public data class CountTrailerResponse(
    val index: Int = 0,
    override val unknownFields: Map<Int, pbandk.UnknownField> = emptyMap()
) : pbandk.Message {
    override operator fun plus(other: pbandk.Message?): com.easyrpc.conformance.v1.CountTrailerResponse = protoMergeImpl(other)
    override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.CountTrailerResponse> get() = Companion.descriptor
    override val protoSize: Int by lazy { super.protoSize }
    public companion object : pbandk.Message.Companion<com.easyrpc.conformance.v1.CountTrailerResponse> {
        public val defaultInstance: com.easyrpc.conformance.v1.CountTrailerResponse by lazy { com.easyrpc.conformance.v1.CountTrailerResponse() }
        override fun decodeWith(u: pbandk.MessageDecoder): com.easyrpc.conformance.v1.CountTrailerResponse = com.easyrpc.conformance.v1.CountTrailerResponse.decodeWithImpl(u)

        override val descriptor: pbandk.MessageDescriptor<com.easyrpc.conformance.v1.CountTrailerResponse> = pbandk.MessageDescriptor(
            fullName = "easyrpc.conformance.v1.CountTrailerResponse",
            messageClass = com.easyrpc.conformance.v1.CountTrailerResponse::class,
            messageCompanion = this,
            fields = buildList(1) {
                add(
                    pbandk.FieldDescriptor(
                        messageDescriptor = this@Companion::descriptor,
                        name = "index",
                        number = 1,
                        type = pbandk.FieldDescriptor.Type.Primitive.Int32(),
                        jsonName = "index",
                        value = com.easyrpc.conformance.v1.CountTrailerResponse::index
                    )
                )
            }
        )
    }
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoRequest")
public fun EchoRequest?.orDefault(): com.easyrpc.conformance.v1.EchoRequest = this ?: EchoRequest.defaultInstance

private fun EchoRequest.protoMergeImpl(plus: pbandk.Message?): EchoRequest = (plus as? EchoRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoRequest {
    var input = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> input = _fieldValue as String
        }
    }

    return EchoRequest(input, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoResponse")
public fun EchoResponse?.orDefault(): com.easyrpc.conformance.v1.EchoResponse = this ?: EchoResponse.defaultInstance

private fun EchoResponse.protoMergeImpl(plus: pbandk.Message?): EchoResponse = (plus as? EchoResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoResponse {
    var output = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> output = _fieldValue as String
        }
    }

    return EchoResponse(output, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCountRequest")
public fun CountRequest?.orDefault(): com.easyrpc.conformance.v1.CountRequest = this ?: CountRequest.defaultInstance

private fun CountRequest.protoMergeImpl(plus: pbandk.Message?): CountRequest = (plus as? CountRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CountRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CountRequest {
    var count = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> count = _fieldValue as Int
        }
    }

    return CountRequest(count, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCountResponse")
public fun CountResponse?.orDefault(): com.easyrpc.conformance.v1.CountResponse = this ?: CountResponse.defaultInstance

private fun CountResponse.protoMergeImpl(plus: pbandk.Message?): CountResponse = (plus as? CountResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CountResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CountResponse {
    var index = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> index = _fieldValue as Int
        }
    }

    return CountResponse(index, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForFailRequest")
public fun FailRequest?.orDefault(): com.easyrpc.conformance.v1.FailRequest = this ?: FailRequest.defaultInstance

private fun FailRequest.protoMergeImpl(plus: pbandk.Message?): FailRequest = (plus as? FailRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun FailRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): FailRequest {
    var message = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> message = _fieldValue as String
        }
    }

    return FailRequest(message, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForFailResponse")
public fun FailResponse?.orDefault(): com.easyrpc.conformance.v1.FailResponse = this ?: FailResponse.defaultInstance

private fun FailResponse.protoMergeImpl(plus: pbandk.Message?): FailResponse = (plus as? FailResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun FailResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): FailResponse {
    var ok = false

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> ok = _fieldValue as Boolean
        }
    }

    return FailResponse(ok, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForHealthRequest")
public fun HealthRequest?.orDefault(): com.easyrpc.conformance.v1.HealthRequest = this ?: HealthRequest.defaultInstance

private fun HealthRequest.protoMergeImpl(plus: pbandk.Message?): HealthRequest = (plus as? HealthRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun HealthRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): HealthRequest {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return HealthRequest(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForHealthResponse")
public fun HealthResponse?.orDefault(): com.easyrpc.conformance.v1.HealthResponse = this ?: HealthResponse.defaultInstance

private fun HealthResponse.protoMergeImpl(plus: pbandk.Message?): HealthResponse = (plus as? HealthResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun HealthResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): HealthResponse {
    var ok = false
    var name = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> ok = _fieldValue as Boolean
            2 -> name = _fieldValue as String
        }
    }

    return HealthResponse(ok, name, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoBytesRequest")
public fun EchoBytesRequest?.orDefault(): com.easyrpc.conformance.v1.EchoBytesRequest = this ?: EchoBytesRequest.defaultInstance

private fun EchoBytesRequest.protoMergeImpl(plus: pbandk.Message?): EchoBytesRequest = (plus as? EchoBytesRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoBytesRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoBytesRequest {
    var data: pbandk.ByteArr = pbandk.ByteArr.empty

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> data = _fieldValue as pbandk.ByteArr
        }
    }

    return EchoBytesRequest(data, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoBytesResponse")
public fun EchoBytesResponse?.orDefault(): com.easyrpc.conformance.v1.EchoBytesResponse = this ?: EchoBytesResponse.defaultInstance

private fun EchoBytesResponse.protoMergeImpl(plus: pbandk.Message?): EchoBytesResponse = (plus as? EchoBytesResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoBytesResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoBytesResponse {
    var data: pbandk.ByteArr = pbandk.ByteArr.empty

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> data = _fieldValue as pbandk.ByteArr
        }
    }

    return EchoBytesResponse(data, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSleepRequest")
public fun SleepRequest?.orDefault(): com.easyrpc.conformance.v1.SleepRequest = this ?: SleepRequest.defaultInstance

private fun SleepRequest.protoMergeImpl(plus: pbandk.Message?): SleepRequest = (plus as? SleepRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun SleepRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): SleepRequest {
    var millis = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> millis = _fieldValue as Int
        }
    }

    return SleepRequest(millis, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForSleepResponse")
public fun SleepResponse?.orDefault(): com.easyrpc.conformance.v1.SleepResponse = this ?: SleepResponse.defaultInstance

private fun SleepResponse.protoMergeImpl(plus: pbandk.Message?): SleepResponse = (plus as? SleepResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun SleepResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): SleepResponse {
    var ok = false

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> ok = _fieldValue as Boolean
        }
    }

    return SleepResponse(ok, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEmptyRequest")
public fun EmptyRequest?.orDefault(): com.easyrpc.conformance.v1.EmptyRequest = this ?: EmptyRequest.defaultInstance

private fun EmptyRequest.protoMergeImpl(plus: pbandk.Message?): EmptyRequest = (plus as? EmptyRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EmptyRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EmptyRequest {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return EmptyRequest(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEmptyResponse")
public fun EmptyResponse?.orDefault(): com.easyrpc.conformance.v1.EmptyResponse = this ?: EmptyResponse.defaultInstance

private fun EmptyResponse.protoMergeImpl(plus: pbandk.Message?): EmptyResponse = (plus as? EmptyResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EmptyResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EmptyResponse {

    val unknownFields = u.readMessage(this) { _, _ -> }

    return EmptyResponse(unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForBigStreamRequest")
public fun BigStreamRequest?.orDefault(): com.easyrpc.conformance.v1.BigStreamRequest = this ?: BigStreamRequest.defaultInstance

private fun BigStreamRequest.protoMergeImpl(plus: pbandk.Message?): BigStreamRequest = (plus as? BigStreamRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun BigStreamRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): BigStreamRequest {
    var count = 0
    var size = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> count = _fieldValue as Int
            2 -> size = _fieldValue as Int
        }
    }

    return BigStreamRequest(count, size, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForBigStreamResponse")
public fun BigStreamResponse?.orDefault(): com.easyrpc.conformance.v1.BigStreamResponse = this ?: BigStreamResponse.defaultInstance

private fun BigStreamResponse.protoMergeImpl(plus: pbandk.Message?): BigStreamResponse = (plus as? BigStreamResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun BigStreamResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): BigStreamResponse {
    var index = 0
    var size = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> index = _fieldValue as Int
            2 -> size = _fieldValue as Int
        }
    }

    return BigStreamResponse(index, size, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForStreamFailRequest")
public fun StreamFailRequest?.orDefault(): com.easyrpc.conformance.v1.StreamFailRequest = this ?: StreamFailRequest.defaultInstance

private fun StreamFailRequest.protoMergeImpl(plus: pbandk.Message?): StreamFailRequest = (plus as? StreamFailRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun StreamFailRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): StreamFailRequest {
    var emitBefore = 0
    var code = 0
    var message = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> emitBefore = _fieldValue as Int
            2 -> code = _fieldValue as Int
            3 -> message = _fieldValue as String
        }
    }

    return StreamFailRequest(emitBefore, code, message, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForStreamFailResponse")
public fun StreamFailResponse?.orDefault(): com.easyrpc.conformance.v1.StreamFailResponse = this ?: StreamFailResponse.defaultInstance

private fun StreamFailResponse.protoMergeImpl(plus: pbandk.Message?): StreamFailResponse = (plus as? StreamFailResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun StreamFailResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): StreamFailResponse {
    var index = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> index = _fieldValue as Int
        }
    }

    return StreamFailResponse(index, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoMetaRequest")
public fun EchoMetaRequest?.orDefault(): com.easyrpc.conformance.v1.EchoMetaRequest = this ?: EchoMetaRequest.defaultInstance

private fun EchoMetaRequest.protoMergeImpl(plus: pbandk.Message?): EchoMetaRequest = (plus as? EchoMetaRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoMetaRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoMetaRequest {
    var input = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> input = _fieldValue as String
        }
    }

    return EchoMetaRequest(input, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoMetaResponse")
public fun EchoMetaResponse?.orDefault(): com.easyrpc.conformance.v1.EchoMetaResponse = this ?: EchoMetaResponse.defaultInstance

private fun EchoMetaResponse.protoMergeImpl(plus: pbandk.Message?): EchoMetaResponse = (plus as? EchoMetaResponse)?.let {
    it.copy(
        meta = meta + plus.meta,
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoMetaResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoMetaResponse {
    var input = ""
    var meta: pbandk.MessageMap.Builder<String, String>? = null

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> input = _fieldValue as String
            2 -> meta = (meta ?: pbandk.MessageMap.Builder()).apply { this.entries += _fieldValue as kotlin.sequences.Sequence<pbandk.MessageMap.Entry<String, String>> }
        }
    }

    return EchoMetaResponse(input, pbandk.MessageMap.Builder.fixed(meta), unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoMetaResponseMetaEntry")
public fun EchoMetaResponse.MetaEntry?.orDefault(): com.easyrpc.conformance.v1.EchoMetaResponse.MetaEntry = this ?: EchoMetaResponse.MetaEntry.defaultInstance

private fun EchoMetaResponse.MetaEntry.protoMergeImpl(plus: pbandk.Message?): EchoMetaResponse.MetaEntry = (plus as? EchoMetaResponse.MetaEntry)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoMetaResponse.MetaEntry.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoMetaResponse.MetaEntry {
    var key = ""
    var value = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> key = _fieldValue as String
            2 -> value = _fieldValue as String
        }
    }

    return EchoMetaResponse.MetaEntry(key, value, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForBigRequest")
public fun BigRequest?.orDefault(): com.easyrpc.conformance.v1.BigRequest = this ?: BigRequest.defaultInstance

private fun BigRequest.protoMergeImpl(plus: pbandk.Message?): BigRequest = (plus as? BigRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun BigRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): BigRequest {
    var size = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> size = _fieldValue as Int
        }
    }

    return BigRequest(size, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForBigResponse")
public fun BigResponse?.orDefault(): com.easyrpc.conformance.v1.BigResponse = this ?: BigResponse.defaultInstance

private fun BigResponse.protoMergeImpl(plus: pbandk.Message?): BigResponse = (plus as? BigResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun BigResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): BigResponse {
    var size = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            2 -> size = _fieldValue as Int
        }
    }

    return BigResponse(size, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForFailDetailsRequest")
public fun FailDetailsRequest?.orDefault(): com.easyrpc.conformance.v1.FailDetailsRequest = this ?: FailDetailsRequest.defaultInstance

private fun FailDetailsRequest.protoMergeImpl(plus: pbandk.Message?): FailDetailsRequest = (plus as? FailDetailsRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun FailDetailsRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): FailDetailsRequest {
    var code = 0
    var message = ""
    var detailType = ""
    var detailText = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> code = _fieldValue as Int
            2 -> message = _fieldValue as String
            3 -> detailType = _fieldValue as String
            4 -> detailText = _fieldValue as String
        }
    }

    return FailDetailsRequest(code, message, detailType, detailText, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForFailDetailsResponse")
public fun FailDetailsResponse?.orDefault(): com.easyrpc.conformance.v1.FailDetailsResponse = this ?: FailDetailsResponse.defaultInstance

private fun FailDetailsResponse.protoMergeImpl(plus: pbandk.Message?): FailDetailsResponse = (plus as? FailDetailsResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun FailDetailsResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): FailDetailsResponse {
    var ok = false

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> ok = _fieldValue as Boolean
        }
    }

    return FailDetailsResponse(ok, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForStreamFailDetailsRequest")
public fun StreamFailDetailsRequest?.orDefault(): com.easyrpc.conformance.v1.StreamFailDetailsRequest = this ?: StreamFailDetailsRequest.defaultInstance

private fun StreamFailDetailsRequest.protoMergeImpl(plus: pbandk.Message?): StreamFailDetailsRequest = (plus as? StreamFailDetailsRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun StreamFailDetailsRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): StreamFailDetailsRequest {
    var emitBefore = 0
    var code = 0
    var message = ""
    var detailType = ""
    var detailText = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> emitBefore = _fieldValue as Int
            2 -> code = _fieldValue as Int
            3 -> message = _fieldValue as String
            4 -> detailType = _fieldValue as String
            5 -> detailText = _fieldValue as String
        }
    }

    return StreamFailDetailsRequest(emitBefore, code, message, detailType,
        detailText, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForStreamFailDetailsResponse")
public fun StreamFailDetailsResponse?.orDefault(): com.easyrpc.conformance.v1.StreamFailDetailsResponse = this ?: StreamFailDetailsResponse.defaultInstance

private fun StreamFailDetailsResponse.protoMergeImpl(plus: pbandk.Message?): StreamFailDetailsResponse = (plus as? StreamFailDetailsResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun StreamFailDetailsResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): StreamFailDetailsResponse {
    var index = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> index = _fieldValue as Int
        }
    }

    return StreamFailDetailsResponse(index, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoTrailerRequest")
public fun EchoTrailerRequest?.orDefault(): com.easyrpc.conformance.v1.EchoTrailerRequest = this ?: EchoTrailerRequest.defaultInstance

private fun EchoTrailerRequest.protoMergeImpl(plus: pbandk.Message?): EchoTrailerRequest = (plus as? EchoTrailerRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoTrailerRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoTrailerRequest {
    var input = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> input = _fieldValue as String
        }
    }

    return EchoTrailerRequest(input, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForEchoTrailerResponse")
public fun EchoTrailerResponse?.orDefault(): com.easyrpc.conformance.v1.EchoTrailerResponse = this ?: EchoTrailerResponse.defaultInstance

private fun EchoTrailerResponse.protoMergeImpl(plus: pbandk.Message?): EchoTrailerResponse = (plus as? EchoTrailerResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun EchoTrailerResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): EchoTrailerResponse {
    var output = ""

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> output = _fieldValue as String
        }
    }

    return EchoTrailerResponse(output, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCountTrailerRequest")
public fun CountTrailerRequest?.orDefault(): com.easyrpc.conformance.v1.CountTrailerRequest = this ?: CountTrailerRequest.defaultInstance

private fun CountTrailerRequest.protoMergeImpl(plus: pbandk.Message?): CountTrailerRequest = (plus as? CountTrailerRequest)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CountTrailerRequest.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CountTrailerRequest {
    var count = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> count = _fieldValue as Int
        }
    }

    return CountTrailerRequest(count, unknownFields)
}

@pbandk.Export
@pbandk.JsName("orDefaultForCountTrailerResponse")
public fun CountTrailerResponse?.orDefault(): com.easyrpc.conformance.v1.CountTrailerResponse = this ?: CountTrailerResponse.defaultInstance

private fun CountTrailerResponse.protoMergeImpl(plus: pbandk.Message?): CountTrailerResponse = (plus as? CountTrailerResponse)?.let {
    it.copy(
        unknownFields = unknownFields + plus.unknownFields
    )
} ?: this

@Suppress("UNCHECKED_CAST")
private fun CountTrailerResponse.Companion.decodeWithImpl(u: pbandk.MessageDecoder): CountTrailerResponse {
    var index = 0

    val unknownFields = u.readMessage(this) { _fieldNumber, _fieldValue ->
        when (_fieldNumber) {
            1 -> index = _fieldValue as Int
        }
    }

    return CountTrailerResponse(index, unknownFields)
}

# easy-rpc Kotlin — mobile client transports

`easy-rpc-kotlin` is now a Kotlin Multiplatform module (targets: `jvm`, `js`,
`wasmJs`, `linuxX64`, `linuxArm64`). This directory holds the **Apple and
Android** transports that are not part of the default build matrix (they need
the Android SDK / Xcode toolchains). Place them into KMP source sets when
building for those platforms:

| File | Source set | Engine | Notes |
|------|------------|--------|-------|
| `CronetTransport.kt` | `androidMain/kotlin/easyrpc/` | Ktor `Cronet` | h1 + h2 + h3; requires `io.ktor:ktor-client-cronet` + Cronet |
| `DarwinTransport.kt`  | `iosMain` / `macosMain` | Ktor `Darwin` | h1 + h2 + h3 via NSURLSession; requires `io.ktor:ktor-client-darwin` |

Both are thin Ktor engine wrappers over the shared `KtorTransport` in
`commonMain` — the framing, trailers, and error handling live in the common
core, so a mobile transport only supplies the engine.

The JVM uses OkHttp (`jvmMain`); native uses Ktor CIO (`nativeMain`); web uses
Ktor's JS engine (`jsMain` / `wasmJsMain`).

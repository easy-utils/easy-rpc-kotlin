// easy-rpc Kotlin — mobile client transports (see README below).
//
// This directory holds Cronet (Android) and Darwin (iOS/macOS) Transport
// implementations. They belong to Kotlin Multiplatform source sets
// (`androidMain` / `iosMain`) and are NOT compiled by the JVM-only
// `easy-rpc-kotlin` build. When lifting this repo into a KMP module, place:
//   - CronetTransport.kt -> androidMain/kotlin/easyrpc/
//   - DarwinTransport.kt  -> iosMain/kotlin/easyrpc/
// and add the matching deps (see README.md).

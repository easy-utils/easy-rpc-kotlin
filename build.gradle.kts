plugins {
    kotlin("jvm") version "2.2.21"
}

group = "easyrpc"
version = "0.1.0"

repositories { mavenCentral() }

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.protobuf:protobuf-javalite:4.34.0")
    implementation("com.google.protobuf:protobuf-kotlin-lite:4.34.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin { jvmToolchain(17) }

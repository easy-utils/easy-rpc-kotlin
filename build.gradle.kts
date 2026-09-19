import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.4.20"
    `maven-publish`
}

group = "io.github.easy-utils"
version = "3.1.0"

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm {
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
        testRuns["test"].executionTask.configure { useJUnitPlatform() }
    }
    js(IR) {
        nodejs()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        nodejs()
    }
    linuxX64()
    linuxArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
                api("io.ktor:ktor-client-core:3.6.0")
                // pbandk: multiplatform protobuf runtime + proto3 JSON (the only
                // runtime with proto3 JSON in commonMain across jvm/native/wasm).
                api("pro.streem.pbandk:pbandk-runtime:0.16.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
            }
        }
        val jvmMain by getting {
            dependencies {
                // OkHttp is the JVM default transport (HTTP/1.1, matches Go net/http).
                implementation("com.squareup.okhttp3:okhttp:4.12.0")
                // Okio zlib for gzip.
                implementation("com.squareup.okio:okio:3.18.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("com.squareup.okhttp3:mockwebserver:4.12.0")
                // Ktor CIO as the alternate JVM transport (matrix `cio`).
                implementation("io.ktor:ktor-client-cio:3.6.0")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:3.6.0")
            }
        }
        val wasmJsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:3.6.0")
            }
        }
        val nativeMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:3.6.0")
                implementation("com.squareup.okio:okio:3.18.2")
            }
        }
    }
}

// ---- publish the multiplatform library (GitHub Packages) ----
publishing {
    publications {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/easy-utils/easy-rpc-kotlin")
                credentials {
                    username = System.getenv("GITHUB_ACTOR") ?: ""
                    password = System.getenv("GITHUB_TOKEN") ?: ""
                }
            }
        }
    }
}

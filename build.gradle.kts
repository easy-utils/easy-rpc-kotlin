plugins {
    kotlin("jvm") version "2.2.21"
    `maven-publish`
}

group = "easyrpc"
version = "0.6.0"

repositories {
    mavenCentral()
    google()
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.protobuf:protobuf-javalite:4.34.0")
    implementation("com.google.protobuf:protobuf-kotlin-lite:4.34.0")
    implementation("io.ktor:ktor-client-cio-jvm:3.3.0")
    // Cronet API is compile-only: applications bring cronet-embedded (JVM)
    // or Play-Services Cronet (Android) themselves. The `cronet` artifact is
    // an AAR (Android packaging) — unpack its classes.jar for the JVM
    // toolchain (classic "unpacked AAR" recipe).
    compileOnly(files(unpackedCronetApi()))
    implementation("io.ktor:ktor-client-core-jvm:3.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin { jvmToolchain(17) }

// The cronet API classes, unpacked for the JVM toolchain (Kotlin cannot
// consume AARs, and Gradle's variant metadata resolves the empty
// `cronet-api` artifact — so fetch the AAR directly from Google Maven).
fun unpackedCronetApi(): File {
    val outDir = layout.buildDirectory.dir("cronet-api").get().asFile
    val jar = File(outDir, "classes.jar")
    if (jar.exists() && jar.length() > 100_000) return jar
    outDir.mkdirs()
    // zip entries carry read-only perms; clear stale outputs before copying
    if (jar.exists()) jar.setWritable(true) && jar.delete()
    val aar = File(outDir, "cronet.aar")
    ant.withGroovyBuilder {
        "get"("src" to "https://dl.google.com/android/maven2/org/chromium/net/cronet/500.0.2/cronet-500.0.2.aar",
              "dest" to aar, "skipExisting" to true)
    }
    copy {
        from(zipTree(aar))
        include("classes.jar")
        into(outDir)
    }
    // gradle copy preserves zip perms (r-x); make it writable for re-runs
    jar.setWritable(true)
    return jar
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "io.github.easy-utils"
            artifactId = "easy-rpc-kotlin"
            version = "0.6.0"
        }
    }
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

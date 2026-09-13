plugins {
    kotlin("jvm") version "2.2.21"
    `maven-publish`
}

group = "easyrpc"
version = "0.2.0"

repositories { mavenCentral() }

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.protobuf:protobuf-javalite:4.34.0")
    implementation("com.google.protobuf:protobuf-kotlin-lite:4.34.0")
    implementation("io.ktor:ktor-client-cio-jvm:3.3.0")
    implementation("io.ktor:ktor-client-core-jvm:3.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

kotlin { jvmToolchain(17) }

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "io.github.easy-utils"
            artifactId = "easy-rpc-kotlin"
            version = "0.2.0"
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

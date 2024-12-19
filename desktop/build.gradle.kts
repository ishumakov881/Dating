plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies{
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp) // Для Android (или другой движок, например, CIO)
    implementation(libs.ktor.client.content.negotiation)

    implementation("io.ktor:ktor-serialization-gson:3.0.1")

    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("io.ktor:ktor-client-serialization:3.0.1")
}
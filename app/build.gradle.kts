import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)


    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    //alias(libs.plugins.google.gms.google.services)
    kotlin("plugin.serialization")
}

fun getConfigValue(key: String, defaultValue: String): String {
    val properties = Properties()
    val configFile = file("C:\\/config.properties")
    if (configFile.exists()) {
        properties.load(configFile.inputStream())
    }
    return properties.getProperty(key, defaultValue)
}

android {
    namespace = "com.lds.quickdeal"
    compileSdk = 35

    val code = versionCodeDate()

    val username = getConfigValue("ACTIVE_DIRECTORY_USERNAME", "")
    val password = getConfigValue("ACTIVE_DIRECTORY_PASSWORD", "")

    defaultConfig {
        applicationId = "com.lds.quickdeal"
        minSdk = 24
        targetSdk = 35
        versionCode = code
        versionName = "1.3.$code"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


//        buildConfigField("String", "ACTIVE_DIRECTORY_USERNAME", "\"${System.getenv("ACTIVE_DIRECTORY_USERNAME") ?: ""}\"")
//        buildConfigField("String", "ACTIVE_DIRECTORY_PASSWORD", "\"${System.getenv("ACTIVE_DIRECTORY_PASSWORD") ?: ""}\"")

    }

    signingConfigs {
        create("config") {
            keyAlias = "release"
            keyPassword = "release"
            storeFile = file("../keystore/keystore.jks")
            storePassword = "release"
        }
    }
    buildTypes {
        getByName("debug") {
            versionNameSuffix = "-DEBUG"
            signingConfig = signingConfigs.getByName("config")



            buildConfigField("String", "ACTIVE_DIRECTORY_USERNAME", "\"$username\"")
            buildConfigField("String", "ACTIVE_DIRECTORY_PASSWORD", "\"$password\"")
        }

        getByName("release") {
            versionNameSuffix = ".release"
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("config")


            buildConfigField("String", "ACTIVE_DIRECTORY_USERNAME", "\"\"")
            buildConfigField("String", "ACTIVE_DIRECTORY_PASSWORD", "\"\"")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    implementation(libs.androidx.material.icons.extended)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("com.darkrockstudios:mpfilepicker:3.1.0")

    val ktor_version = "3.0.1"
    implementation("io.ktor:ktor-client-logging:$ktor_version") // Для логов

    implementation("com.unboundid:unboundid-ldapsdk:6.0.6")


    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

//    implementation("io.ktor:ktor-client-core:2.3.2")
//    implementation("io.ktor:ktor-client-okhttp:2.3.2") // Для Android (или другой движок, например, CIO)
//    implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
//    implementation("io.ktor:ktor-serialization-gson:2.3.2")
//    implementation("io.ktor:ktor-client-logging:2.3.2") // Для логов

    implementation("io.ktor:ktor-client-core:3.0.1")
    implementation("io.ktor:ktor-client-okhttp:3.0.1") // Для Android (или другой движок, например, CIO)
    implementation("io.ktor:ktor-client-content-negotiation:3.0.1")
    implementation("io.ktor:ktor-serialization-gson:3.0.1")

    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("io.ktor:ktor-client-serialization:3.0.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
fun versionCodeDate(): Int {
    val dateFormat = SimpleDateFormat("yyMMdd")
    return dateFormat.format(Date()).toInt()
}
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")

    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("kotlin-kapt")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.shared)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "com.lds.quickdeal"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.lds.quickdeal"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)


    implementation("com.github.ygorluizfrazao.compose-audio-controls:audio-services:v1.0.3-alpha01")
    implementation("com.github.ygorluizfrazao.compose-audio-controls:ui:v1.0.3-alpha01")
    //implementation(libs.richeditor.compose)
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)
    implementation(project(":shared"))
    ksp(libs.androidx.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.androidx.runtime.livedata)
    debugImplementation(libs.leakcanary.android)

    //implementation("androidx.activity:activity-compose:1.9.3")
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.material.icons.extended)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(libs.mpfilepicker)
    implementation(libs.play.services.location)


    implementation(libs.ktor.client.logging) // Для логов

    implementation(libs.unboundid.ldapsdk)


    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

//    implementation("io.ktor:ktor-client-core:2.3.2")
//    implementation("io.ktor:ktor-client-okhttp:2.3.2") // Для Android (или другой движок, например, CIO)
//    implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
//    implementation("io.ktor:ktor-serialization-gson:2.3.2")
//    implementation("io.ktor:ktor-client-logging:2.3.2") // Для логов

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp) // Для Android (или другой движок, например, CIO)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.gson)

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

    //implementation ("com.google.accompanist:accompanist-swipetodismiss:0.31.2-alpha")
    //noinspection UseTomlInstead
    implementation("com.github.SirLordPouya.AndroidAppUpdater:compose:10.0.0")

    implementation(libs.coil.compose)
    implementation("io.coil-kt.coil3:coil-network-ktor2:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.4")
}

compose.desktop {
    application {
        mainClass = "com.lds.quickdeal.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.lds.quickdeal"
            packageVersion = "1.0.0"
        }
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    kotlin("plugin.serialization") version "2.1.0" apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    //kotlin("plugin.serialization") version "1.9.10"

    //alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false

    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
}

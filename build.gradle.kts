// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}


buildscript {

    repositories {
        google()
        mavenCentral()
    }

    extra["compose_version"] = "1.1.0"
    dependencies {
        classpath(libs.hilt.android.gradle.plugin)
        classpath(libs.gradle)
        classpath(libs.kotlin.gradle.plugin)
    }
}
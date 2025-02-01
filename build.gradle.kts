// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle) // Specify the version of the Kotlin DSL plugin
        classpath(libs.kotlin.gradle.plugin) // Replace with the latest version
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
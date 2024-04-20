// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val navVersion = "2.5.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46")
        classpath("com.google.gms:google-services:4.3.15")
    }
}
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.1.0" apply false
}

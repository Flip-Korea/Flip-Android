// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(libs.plugins.android.application.get().pluginId) version libs.versions.androidGradlePlugin apply false
    id(libs.plugins.kotlin.android.get().pluginId) version libs.versions.kotlin apply false
    id(libs.plugins.android.library.get().pluginId) version libs.versions.androidGradlePlugin apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.hilt.gradle.plugin)
    }
}
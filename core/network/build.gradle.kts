import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

val baseUrl: String = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
val apiKey: String = gradleLocalProperties(rootDir, providers).getProperty("API_KEY")

android {
    namespace = "com.rodev.mmf_timetable.core.network"

    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("String", "BASE_URL", baseUrl)
        buildConfigField("String", "API_KEY", apiKey)
    }
}
dependencies {
    api(projects.core.common)
    api(projects.core.model)
    api(libs.kotlin.datetime)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.log)
}
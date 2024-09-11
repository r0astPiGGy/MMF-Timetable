plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.rodev.mmf_timetable.core.network"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    api(libs.kotlin.datetime)

    implementation(libs.kotlin.serialization.json)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.log)
}
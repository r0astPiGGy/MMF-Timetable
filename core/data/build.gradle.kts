plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.rodev.mmf_timetable.core.data"
}

dependencies {
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.network)
    implementation(libs.kotlinx.serialization.json)
}
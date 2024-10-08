plugins {
    alias(libs.plugins.mmf.timetable.android.feature)
    alias(libs.plugins.mmf.timetable.android.libraryCompose)
}

android {
    namespace = "com.rodev.mmf_timetable.feature.timetable"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
}
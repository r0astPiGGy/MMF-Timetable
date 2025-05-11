plugins {
    alias(libs.plugins.mmf.timetable.android.feature)
    alias(libs.plugins.mmf.timetable.android.libraryCompose)
}

android {
    namespace = "com.rodev.mmf_timetable.feature.teachers"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.domain)
}


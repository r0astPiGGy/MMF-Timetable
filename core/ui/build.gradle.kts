plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.libraryCompose)
}

android {
    namespace = "com.rodev.mmf_timetable.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    api(projects.core.model)
    implementation(libs.android.icons)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}
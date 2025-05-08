plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.libraryCompose)
}

android {
    namespace = "com.rodev.mmf_timetable.core.designsystem"
}

dependencies {
    api(libs.android.activity.compose)
    api(libs.android.compose.ui)
    api(libs.android.compose.ui.graphics)
    api(libs.material)
    api(libs.android.compose.material3)
    implementation(libs.android.icons)
}
plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.hilt)
}

android {
    namespace = "com.rodev.mmf_timetable.core.network"
}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
}
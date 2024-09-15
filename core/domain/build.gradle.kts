plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.hilt)
}

android {
    namespace = "com.rodev.mmf_timetable.core.domain"
}

dependencies {
    api(projects.core.model)

    implementation(projects.core.data)
}
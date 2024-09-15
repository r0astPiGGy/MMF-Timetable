plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.hilt)
}

android {
    namespace = "com.rodev.mmf_timetable.widget"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.designsystem) // ?

    implementation(libs.android.glance.appwidget)
    implementation(libs.android.glance.material3)
    implementation(libs.google.gson)
}
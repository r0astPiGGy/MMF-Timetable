plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.mmf.timetable.android.hilt)
}

android {
    namespace = "com.rodev.mmf_timetable.core.datastore"
}

dependencies {
    api(libs.android.datastore.datastore)
    api(projects.core.datastoreProto)
    api(projects.core.model)

    implementation(projects.core.common)
}
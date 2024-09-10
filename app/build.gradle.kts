plugins {
    alias(libs.plugins.mmf.timetable.android.application)
    alias(libs.plugins.mmf.timetable.android.hilt)
    alias(libs.plugins.mmf.timetable.android.applicationCompose)
}

android {
    defaultConfig {
        applicationId = "com.rodev.mmf_timetable"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.rodev.mmf_timetable"
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.android.core)

    implementation(libs.android.lifecycle.runtime.ktx)
    implementation(libs.android.lifecycle.runtime.compose)

    implementation(libs.android.activity.compose)

    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.graphics)
    implementation(libs.material)
    implementation(libs.android.compose.material3)

    implementation(libs.android.datastore.datastore)

    api(projects.core.datastore)

    // Testing
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.android.junit)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.android.compose.junit)
    debugImplementation(libs.android.compose.ui.tooling.preview)
    debugImplementation(libs.test.android.compose.manifest)

    // Jsoup
    implementation("org.jsoup:jsoup:1.16.1")

    // Prefs
    implementation(libs.github.compose.prefs)
    implementation(libs.android.datastore.preferences)

    // ViewModel One-time events
    implementation(libs.github.compose.state.events)

    // Glance widget's
    implementation(libs.android.glance.appwidget)
    implementation(libs.android.glance.material3)
    implementation(libs.google.gson)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

}
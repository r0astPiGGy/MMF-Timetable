plugins {
    alias(libs.plugins.mmf.timetable.android.application)
    alias(libs.plugins.mmf.timetable.android.applicationCompose)
    alias(libs.plugins.mmf.timetable.android.hilt)
    alias(libs.plugins.kotlin.serialization)
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
    implementation(projects.feature.timetable)
    implementation(projects.feature.settings)
    implementation(projects.feature.home)
    implementation(projects.feature.preferences)

    implementation(projects.widget)

    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.ui)

    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.android.icons)
    implementation(libs.android.core)
    implementation(libs.android.lifecycle.runtime.ktx)
    implementation(libs.android.lifecycle.runtime.compose)
    implementation(libs.android.activity.compose)

    implementation(libs.android.hilt.navigation.compose)
    implementation(libs.android.navigation.compose)
    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.graphics)
    implementation(libs.material)
    implementation(libs.android.compose.material3)

    // Prefs
    implementation(libs.github.compose.prefs)
    implementation(libs.android.datastore.preferences)

    // ViewModel One-time events
    implementation(libs.github.compose.state.events)

//    ksp(libs.android.hilt.compiler)

    implementation(libs.kotlinx.serialization.json)
    // Testing
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.android.junit)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.android.compose.junit)
    debugImplementation(libs.android.compose.ui.tooling.preview)
    debugImplementation(libs.test.android.compose.manifest)
}
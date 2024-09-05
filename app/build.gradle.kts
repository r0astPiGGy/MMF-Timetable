plugins {
    alias(libs.plugins.android.application) 
    alias(libs.plugins.dagger.hilt) 
    alias(libs.plugins.android.kotlin) 
    alias(libs.plugins.google.protobuf) 
    alias(libs.plugins.ksp) 
}

android {
    namespace = "com.rodev.mmf_timetable"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rodev.mmf_timetable"
        minSdk = 28
        targetSdk = 33
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.2"
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.android.core)

    implementation(libs.android.lifecycle.runtime.ktx)
    implementation(libs.android.lifecycle.runtime.compose)

    implementation(libs.android.activity.compose)

    implementation(platform(libs.android.compose.bom))
    implementation(libs.android.compose.ui)
    implementation(libs.android.compose.ui.graphics)
    implementation(libs.android.compose.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.android.compose.material3)

    implementation(libs.android.datastore.datastore)
    implementation(libs.kotlin.protobuf)

    // Testing
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.android.junit)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.android.compose.junit)
    androidTestImplementation(platform(libs.android.compose.bom))
    debugImplementation(libs.android.compose.ui.tooling.preview)
    debugImplementation(libs.test.android.compose.manifest)

    // Jsoup
    implementation("org.jsoup:jsoup:1.16.1")

    // Prefs
    implementation(libs.github.compose.prefs)
    implementation(libs.android.datastore.preferences)

    // Dagger Hilt
    implementation(libs.android.hilt.navigation.compose)
    implementation(libs.android.hilt.dagger)
    ksp(libs.android.hilt.compiler)

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
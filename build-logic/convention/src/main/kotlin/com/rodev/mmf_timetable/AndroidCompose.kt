package com.rodev.mmf_timetable

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    extension: CommonExtension<*, *, *, *, *, *>
) {
    with(extension) {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.4.3"
        }

        dependencies {
            val bom = libs.findLibrary("android-compose-bom").get()

            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation", libs.findLibrary("android-compose-ui-tooling-preview").get())
        }
    }

}
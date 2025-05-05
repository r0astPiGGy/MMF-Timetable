import com.rodev.mmf_timetable.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "mmf_timetable.android.library")
            apply(plugin = "mmf_timetable.android.hilt")
            apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:common"))

                "implementation"(libs.findLibrary("android.hilt.navigation.compose").get())
                "implementation"(libs.findLibrary("android.lifecycle.runtime.compose").get())
                "implementation"(libs.findLibrary("android.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}
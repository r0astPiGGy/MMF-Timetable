import com.android.build.gradle.LibraryExtension
import com.rodev.mmf_timetable.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("mmf_timetable.android.library")
                apply("mmf_timetable.android.hilt")
            }

            dependencies {
//                add("implementation", project(":core:ui"))
//                add("implementation", project(":core:designsystem"))

                add("implementation", libs.findLibrary("android.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("android.lifecycle.runtime.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
            }
        }
    }
}
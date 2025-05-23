// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.compose.kotlin) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.google.protobuf) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.dagger.hilt) apply false
}

allprojects {
    tasks.register<DependencyReportTask>("printAllDependencies") {}
}

subprojects {
    configurations.all {
        resolutionStrategy {
            force("androidx.navigation:navigation-compose:2.8.9")
        }
    }
}

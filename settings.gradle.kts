pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(url = "https://jitpack.io")
        mavenCentral()
    }
}

rootProject.name = "MMF_Timetable"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

include(":core:datastore")
include(":core:model")
include(":core:common")
include(":core:data")
include(":core:datastore-proto")
include(":core:database")
include(":core:domain")
include(":core:designsystem")
include(":core:network")
include(":core:ui")

include(":feature:classrooms")
include(":feature:teachers")
include(":feature:timetable")
include(":feature:preferences")
include(":feature:home")
include(":feature:settings")

include(":widget")

plugins {
    alias(libs.plugins.mmf.timetable.android.library)
    alias(libs.plugins.google.protobuf)
}

android {
    namespace = "com.rodev.mmf_timetable.core.datastore.proto"
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
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
    api(libs.kotlin.protobuf)
}
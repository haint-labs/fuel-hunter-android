import com.google.protobuf.gradle.*

plugins {
    java
    idea
    kotlin("jvm")
    id(Build.Plugins.ID.protobuf)
}

dependencies {
    protobuf("com.github.haint-labs:fuel-hunter-proto:51fca59")

    implementation("javax.annotation:javax.annotation-api:1.3.2")
    api("com.google.protobuf:protobuf-javalite:3.12.0")
    api("io.grpc:grpc-kotlin-stub-lite:0.2.1")
    api("io.grpc:grpc-okhttp:1.32.1")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.13.0"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.32.1"
        }

        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:0.2.0:jdk7@jar"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.builtins {
                named("java") {
                    option("lite")
                }
            }

            it.plugins {
                id("grpc") {
                    option("lite")

                    outputSubDir = "grpc"
                }

                id("grpckt") {
                    option("lite")
                }
            }
        }
    }
}
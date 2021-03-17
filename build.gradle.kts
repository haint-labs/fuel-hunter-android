import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Build.Plugins.android)
        classpath(Build.Plugins.kotlin)
        classpath(Build.Plugins.googleServices)
        classpath(Build.Plugins.crashlytics)
        classpath(Build.Plugins.protobuf)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.register("clean").configure {
    delete("build")
}

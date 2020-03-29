import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(Build.Plugins.android)
        classpath(Build.Plugins.kotlin)
        classpath(Build.Plugins.googleServices)
        classpath(Build.Plugins.crashlytics)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.register("clean").configure {
    delete("build")
}

buildscript {
    repositories {
        google()
        jcenter()
        
    }

    dependencies {
        classpath(Build.Plugins.android)
        classpath(Build.Plugins.kotlin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean").configure {
    delete("build")
}

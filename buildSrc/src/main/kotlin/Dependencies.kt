@file:Suppress("ClassName")

const val junit = "junit:junit:4.12"

object kt {
    const val version = "1.3.61"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
}

object androidx {
    private const val version = "1.2.0"
    private const val lifecycleVersion = "2.2.0"

    const val core = "androidx.core:core-ktx:${version}"
    const val appCompat = "androidx.appcompat:appcompat:1.1.0"

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
    const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:2.2.1"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:2.2.1"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata:$lifecycleVersion"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:$lifecycleVersion"

    const val testRunner = "androidx.test:runner:1.2.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
}

object firebase {
    const val crashlytics = "com.google.firebase:firebase-crashlytics:17.0.0-beta02"
}

object Build {
    object Plugins {
        const val android = "com.android.tools.build:gradle:3.5.3"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${kt.version}"
        const val googleServices = "com.google.gms:google-services:4.3.3"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:2.0.0-beta03"

        object ID {
            const val androidApp = "com.android.application"
            const val ktAndroid = "kotlin-android"
            const val ktAndroidExt = "kotlin-android-extensions"
            const val kapt = "kotlin-kapt"
            const val googleServices = "com.google.gms.google-services"
            const val crashlytics = "com.google.firebase.crashlytics"
        }
    }
}



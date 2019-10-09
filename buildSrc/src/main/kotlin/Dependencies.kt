@file:Suppress("ClassName")

const val junit = "junit:junit:4.12"

object kt {
    const val version = "1.3.50"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
}

object androidx {
    private const val version = "1.1.0"

    const val core = "androidx.core:core-ktx:${version}"
    const val appCompat = "androidx.appcompat:appcompat:${version}"

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0-beta04"
    const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:2.2.0-alpha02"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:2.2.0-alpha02"

    const val testRunner = "androidx.test:runner:1.2.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
}

object Build {
    object Plugins {
        const val android = "com.android.tools.build:gradle:3.5.1"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${kt.version}"

        object ID {
            const val androidApp = "com.android.application"
            const val ktAndroid = "kotlin-android"
            const val ktAndroidExt = "kotlin-android-extensions"
            const val kapt = "kotlin-kapt"
        }
    }
}



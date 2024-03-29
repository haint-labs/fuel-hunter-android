@file:Suppress("ClassName")

const val junit = "junit:junit:4.12"

object kt {
    const val version = "1.4.32"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"

    object coroutines {
        private const val version = "1.4.0-M1"

        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
    }

}

object androidx {
    private const val version = "1.3.1"
    private const val lifecycleVersion = "2.2.0"
    private const val fragmentVersion = "1.3.0-beta01"

    const val core = "androidx.core:core-ktx:${version}"
    const val appCompat = "androidx.appcompat:appcompat:1.2.0"

    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
    const val legacy = "androidx.legacy:legacy-support-v4:1.0.0"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:2.3.1"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:2.3.1"

    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
    const val lifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion"
    const val lifecycleLiveData = "androidx.lifecycle:lifecycle-livedata:$lifecycleVersion"
    const val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:$lifecycleVersion"

    const val fragmentKtx = "androidx.fragment:fragment-ktx:${fragmentVersion}"

    const val dataStore = "androidx.datastore:datastore-core:1.0.0-alpha01"

    const val testRunner = "androidx.test:runner:1.2.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
}

object openStreet {
    const val map = "org.osmdroid:osmdroid-android:6.1.8"
}

object compose {
    const val version = "1.0.0-beta05"

    const val foundation = "androidx.compose.foundation:foundation:$version"
    const val runtime = "androidx.compose.runtime:runtime:$version"
    const val layout = "androidx.compose.foundation:foundation-layout:$version"
    const val material = "androidx.compose.material:material:$version"
    const val tooling = "androidx.compose.ui:ui-tooling:$version"
    const val navigation = "androidx.navigation:navigation-compose:1.0.0-alpha10"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha05"
}

object firebase {
    const val crashlytics = "com.google.firebase:firebase-crashlytics:17.2.1"
}

object accompanist {
    private const val version = "0.7.1"

    const val coil = "com.google.accompanist:accompanist-coil:$version"
    const val pager = "com.google.accompanist:accompanist-pager:$version"
    const val insets = "com.google.accompanist:accompanist-insets:$version"
}

object Build {
    object Plugins {
        const val android = "com.android.tools.build:gradle:7.0.0-alpha12"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${kt.version}"
        const val googleServices = "com.google.gms:google-services:4.3.3"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-gradle:2.2.1"
        const val protobuf = "com.google.protobuf:protobuf-gradle-plugin:0.8.13"

        object ID {
            const val androidApp = "com.android.application"
            const val ktAndroid = "kotlin-android"
            const val kapt = "kotlin-kapt"
            const val googleServices = "com.google.gms.google-services"
            const val crashlytics = "com.google.firebase.crashlytics"
            const val protobuf = "com.google.protobuf"
        }
    }
}



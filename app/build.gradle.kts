plugins {
    id(Build.Plugins.ID.androidApp)
    id(Build.Plugins.ID.ktAndroid)
    id(Build.Plugins.ID.ktAndroidExt)
    id(Build.Plugins.ID.kapt)
    id(Build.Plugins.ID.googleServices)
    id(Build.Plugins.ID.crashlytics)
}

android {
    compileSdkVersion(29)

    buildToolsVersion = "29.0.3"

    defaultConfig {
        applicationId = "fuel.hunter"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    dataBinding {
        isEnabled = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(kt.stdlib)

    implementation(androidx.core)
    implementation(androidx.appCompat)
    implementation(androidx.constraintLayout)
    implementation(androidx.recyclerView)
    implementation(androidx.legacy)
    implementation(androidx.navigationFragment)
    implementation(androidx.navigationUi)

    implementation(androidx.lifecycleViewModel)
    implementation(androidx.lifecycleLiveData)
    implementation(androidx.lifecycleExtensions)
    implementation(androidx.lifecycleRuntime)

    implementation(firebase.crashlytics)

    testImplementation(junit)

    androidTestImplementation(androidx.testRunner)
    androidTestImplementation(androidx.espressoCore)
}

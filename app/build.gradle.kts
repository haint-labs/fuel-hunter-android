import com.google.protobuf.gradle.*

plugins {
    id(Build.Plugins.ID.androidApp)
    id(Build.Plugins.ID.ktAndroid)
    id(Build.Plugins.ID.kapt)
    id(Build.Plugins.ID.googleServices)
    id(Build.Plugins.ID.crashlytics)
    id(Build.Plugins.ID.protobuf)
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "fuel.hunter"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        compose = true
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = compose.version
    }
}

dependencies {
    implementation(kt.stdlib)
    implementation(kt.coroutines.core)
    implementation(kt.coroutines.android)

    implementation(accompanist.coil)
    implementation(accompanist.pager)
    implementation(accompanist.insets)

    implementation(project(":fh-client"))

    implementation(androidx.core)
    implementation(androidx.appCompat)
    implementation(androidx.constraintLayout)
    implementation(androidx.recyclerView)
    implementation(androidx.legacy)
    implementation(androidx.navigationFragment)
    implementation(androidx.navigationUi)

    implementation(androidx.lifecycleViewModel)
    implementation(androidx.lifecycleLiveData)
    implementation(androidx.lifecycleLiveDataKtx)
    implementation(androidx.lifecycleExtensions)
    implementation(androidx.lifecycleRuntime)

    implementation(openStreet.map)

    implementation(compose.foundation)
    implementation(compose.constraintLayout)
    implementation(compose.runtime)
    implementation(compose.tooling)
    implementation(compose.material)
    implementation(compose.navigation)

    implementation(androidx.dataStore)
    implementation("com.google.protobuf:protobuf-javalite:3.12.0")

    implementation(androidx.fragmentKtx)

    implementation(firebase.crashlytics)

    testImplementation(junit)

    androidTestImplementation(androidx.testRunner)
    androidTestImplementation(androidx.espressoCore)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.13.0"
    }

    generateProtoTasks {
        all().forEach {
            it.builtins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.vectorincng.squareandroidtaskvictor"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.vectorincng.squareandroidtaskvictor"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.vectorincng.squareandroidtaskvictor.utilities.MainTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["dagger.hilt.disableModulesHaveInstallInCheck"] = "true"
            }
        }
    }

    buildTypes {
        release {
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

        // Enable Coroutines and Flow APIs
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.FlowPreview"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }

    testOptions {
        packagingOptions {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.lifecycle.runtime.compose)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.material)
    implementation(libs.gson)

    // Networking
    implementation(libs.okhttp3)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp3.logging.interceptor)

    // Image loading
    implementation(libs.coil.kt.compose)

    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.glide)

    testImplementation(libs.junit)
    testImplementation(libs.turbine.android)
    testImplementation(libs.mock.mockk.android)
    testImplementation(libs.mock.mockk.agent)


    androidTestImplementation(libs.turbine.android)
    androidTestImplementation(libs.mock.mockk.android)
    androidTestImplementation(libs.mock.mockk.agent)

    debugImplementation(libs.androidx.monitor)
    kspAndroidTest(libs.hilt.android.compiler)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.uiautomator)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.guava)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.accessibility.test.framework)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
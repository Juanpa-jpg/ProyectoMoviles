plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"
}

android {
    namespace = "com.example.cazadordepalabras"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cazadordepalabras"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Compose actualizado para Kotlin 2.0
    implementation("androidx.compose.ui:ui:1.5.11")
    implementation("androidx.compose.material:material:1.5.11")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.11")
    implementation("androidx.compose.foundation:foundation:1.5.11")

    // Navegación Compose
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Retrofit + Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.google.code.gson:gson:2.10.1")


    // Material 3 desde `libs` (esto depende de tu configuración, puede comentarse si da error)
    implementation(libs.androidx.material3.android)

    // Debug tools
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.11")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.11")
}


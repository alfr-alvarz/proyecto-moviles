plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // KSP 1.9.23-1.0.19 es correcto para Kotlin 1.9.23
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
}

android {
    namespace = "com.example.tiendaguaumiau"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tiendaguaumiau"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        // 1.5.11 es la versión correcta para Kotlin 1.9.23
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // --- Versiones ---
    val room_version = "2.6.1"
    val retrofit_version = "2.9.0"
    val moshi_version = "1.15.1"

    // --- Base de Datos (Room) ---
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    // --- Networking (Retrofit & Moshi) ---
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    // Converter para usar Moshi con Retrofit
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit_version")
    // Librería principal de Moshi
    implementation("com.squareup.moshi:moshi-kotlin:$moshi_version")
    // Generador de código de Moshi (usando KSP)
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshi_version")

    // --- Compose (BOM 2024.04.00) ---
    implementation(platform("androidx.compose:compose-bom:2024.04.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))

    // --- Android KTX ---
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose")

    // --- UI Compose ---
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material3:material3-window-size-class")

    // --- Navegación y Lifecycle ---
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // --- Coroutines ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // --- Tests ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
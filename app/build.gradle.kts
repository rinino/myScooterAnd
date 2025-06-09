// myScooter/app/build.gradle
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") // Lascia solo KSP per la generazione del codice di Room
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "it.adr.myscooter"
    compileSdk = 35

    defaultConfig {
        applicationId = "it.adr.myscooter"
        minSdk = 24
        targetSdk = 35 // Allineato a compileSdk per la massima compatibilità
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        // Aggiornato per compatibilità con Kotlin 2.0.0
        kotlinCompilerExtensionVersion = "1.5.12"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    // buildToolsVersion non è più necessario specificarlo manualmente con AGP recenti
}

dependencies {

    // Versione più recente stabile di KTX
    implementation("androidx.core:core-ktx:1.16.0")
    // Versione più recente stabile
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    // Versione più recente stabile
    implementation("androidx.activity:activity-compose:1.10.1")
    // Usa l'ultima versione STABILE del BOM di Compose (attualmente 2024.06.00)
    implementation(platform("androidx.compose:compose-bom:2025.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    // Per icone Material Design Extended (allineata con il BOM se possibile, altrimenti ultima stabile)
    implementation("androidx.compose.material:material-icons-extended:1.7.8") // Lasciata questa versione che è più comune

    // Room (per il database)
    // Se la versione 2.7.1 ti dà ancora problemi, potresti provare a scendere alla 2.6.1 che è molto stabile
    implementation("androidx.room:room-runtime:2.7.1")
    ksp("androidx.room:room-compiler:2.7.1") // KSP compiler deve corrispondere alla versione di Room
    implementation("androidx.room:room-ktx:2.7.1")

    // Lifecycle ViewModel per Compose (allineata con lifecycle-runtime-ktx)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.1")

    // Navigation Compose (allineata con la versione più recente stabile)
    implementation("androidx.navigation:navigation-compose:2.9.0") // L'ultima stabile è 2.7.7, la 2.9.0 è sperimentale

    // Coil (per caricare immagini)
    implementation("io.coil-kt:coil-compose:2.6.0") // L'ultima stabile è 2.6.0

    // Per PhotosPicker (Activity Result API) - allineata con activity-compose
    implementation("androidx.activity:activity-ktx:1.10.1")

    // Per Persistent Media (Activity Result Contracts)
    implementation("androidx.media3:media3-exoplayer:1.7.1")

    // Dipendenze di test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
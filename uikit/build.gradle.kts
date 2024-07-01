plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "com.natiqhaciyef.uikit"
    compileSdk = 34

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        freeCompilerArgs += "-Xjvm-default=all"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    val retrofitVersion = "2.9.0"
    val coilVersion = "2.5.0"
    val cameraXVersion = "1.3.1"
    val mlKitBarcodeVersion = "18.3.0"
    val mlKitRecognizerVersion = "19.0.0"
    val scannerMlKitVersion = "16.0.0-beta1"
    val pdfViewerVersion = "2.3.0"
    val fragmentVersion = "1.5.2"
    val lifecycleVersion = "2.5.1"
    val dataStoreVersion = "1.0.0"

    implementation(project(":common"))
    implementation(project(":core"))
    implementation(project(":domain"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")

    // Gson
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // Fingerprint
    implementation("androidx.biometric:biometric:1.2.0-alpha05")

    // Camera X
    implementation("androidx.camera:camera-core:${cameraXVersion}")
    implementation("androidx.camera:camera-camera2:${cameraXVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraXVersion}")
    implementation("androidx.camera:camera-video:${cameraXVersion}")
    implementation("androidx.camera:camera-view:${cameraXVersion}")
    implementation("androidx.camera:camera-extensions:${cameraXVersion}")

    // Android Barcode scanner and Text recognizer ml kit
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:${mlKitBarcodeVersion}")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:$mlKitRecognizerVersion")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-common:$mlKitRecognizerVersion")
    implementation("com.google.android.gms:play-services-mlkit-document-scanner:${scannerMlKitVersion}")
//    implementation("com.google.firebase:firebase-ml-vision:24.1.0")   R4s.6zV3LruyE7K
//    implementation("com.google.android.gms:play-services-vision:19.0.0")

    // Image & PdfViewer
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("com.github.barteksc:android-pdf-viewer:$pdfViewerVersion")

    //Fragment ktx
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    //Data store
    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")
    implementation("androidx.security:security-crypto:$dataStoreVersion")

}
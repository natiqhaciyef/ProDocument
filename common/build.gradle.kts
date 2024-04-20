plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.natiqhaciyef.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

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
    }
}

dependencies {
    val lottieVersion = "5.2.0"
    val coilVersion = "2.5.0"
    val mlKitBarcodeVersion = "18.3.0"
    val mlKitRecognizerVersion = "19.0.0"
    val scannerMlKitVersion = "16.0.0-beta1"
    val cameraXVersion = "1.3.1"
    val pdfViewerVersion = "2.3.0"
    val retrofitVersion = "2.9.0"
    val okHttpLoggingInterceptorVersion = "5.0.0-alpha.3"

    
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("com.airbnb.android:lottie:$lottieVersion")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.1.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
//    implementation("com.google.firebase:firebase-storage-ktx")

    implementation("androidx.camera:camera-core:${cameraXVersion}")
    implementation("androidx.camera:camera-camera2:${cameraXVersion}")
    implementation("androidx.camera:camera-lifecycle:${cameraXVersion}")
    implementation("androidx.camera:camera-video:${cameraXVersion}")
    implementation("androidx.camera:camera-view:${cameraXVersion}")
    implementation("androidx.camera:camera-extensions:${cameraXVersion}")
    implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:${mlKitBarcodeVersion}")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:$mlKitRecognizerVersion")
    implementation("com.google.android.gms:play-services-mlkit-text-recognition-common:$mlKitRecognizerVersion")
    implementation("com.google.android.gms:play-services-mlkit-document-scanner:${scannerMlKitVersion}")

    implementation("com.github.barteksc:android-pdf-viewer:$pdfViewerVersion")


    //Retrofit library
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpLoggingInterceptorVersion")
}
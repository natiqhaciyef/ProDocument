plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.natiqhaciyef.prodocument"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.natiqhaciyef.prodocument"
        minSdk = 28
        targetSdk = 33
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xjvm-default=all"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    val fragmentVersion = "1.5.2"
    val retrofitVersion = "2.9.0"
    val daggerVersion = "2.46"
    val epoxyVersion = "5.1.3"
    val lifecycleVersion = "2.5.1"
    val cameraXVersion = "1.3.1"
    val mlKitBarcodeVersion = "18.3.0"
    val mlKitRecognizerVersion = "19.0.0"
    val viewPagerVersion = "1.0.0"
    val scannerMlKitVersion = "16.0.0-beta1"
    val zxingVersion = "4.3.0"
    val dataStoreVersion = "1.0.0"
    val coilVersion = "2.5.0"
    val okHttpLoggingInterceptorVersion = "5.0.0-alpha.3"
    val lottieVersion = "5.2.0"
    val navigationVersion = "2.5.3"
    val encryptedSharedPrefVersion = "1.1.0-alpha06"
    val pdfViewerVersion = "2.3.0"
    val workManagerVersion = "2.8.1"
    val swipeLayoutVersion = "1.1.0"

    // scanner .aar file integration
//    implementation(files(mapOf("dir" to "libs", "include" to listOf("scanlibrary.aar"))))


    implementation("com.google.ar:core:1.30.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Epoxy UI && Masked text
    implementation("com.airbnb.android:epoxy:$epoxyVersion")
    annotationProcessor("com.airbnb.android:epoxy-processor:$epoxyVersion")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:31.1.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
//    implementation("com.google.firebase:firebase-storage-ktx")

    //Room db
    implementation("androidx.room:room-runtime:2.5.0")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.5.0")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")

    // Encrypted Shared Preference
    implementation("androidx.security:security-crypto:${encryptedSharedPrefVersion}")

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


    //Zxing (scanner)
    implementation("com.journeyapps:zxing-android-embedded:$zxingVersion")

    //Data store
    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")
    implementation("androidx.security:security-crypto:$dataStoreVersion")

    //Fragment ktx
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    //View Model Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    //Image & document loader library
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("com.github.barteksc:android-pdf-viewer:$pdfViewerVersion")

    //Retrofit library
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpLoggingInterceptorVersion")

    //Dagger hilt
    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerVersion")

    //Animation Library & Swipe Refresh Layout & ViewPager
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swipeLayoutVersion")
    implementation("com.airbnb.android:lottie:$lottieVersion")
    implementation("androidx.viewpager2:viewpager2:$viewPagerVersion")

    // Work manager
    implementation("androidx.work:work-runtime-ktx:$workManagerVersion")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    // TestImplementations
    implementation("androidx.test:core:1.5.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.robolectric:robolectric:4.8.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("org.mockito:mockito-core:4.7.0")

    // Android Test Implementations
    androidTestImplementation("junit:junit:4.13.2")
    //androidTestImplementation "com.linkedin.dexmaker:dexmaker-mockito:2.12.1"
    androidTestImplementation("org.mockito:mockito-android:4.7.0")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("org.mockito:mockito-core:4.7.0")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.43.2")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$daggerVersion")
    debugImplementation("androidx.fragment:fragment-testing:1.3.0-alpha08")
    kaptTest("com.google.dagger:hilt-android-compiler:$daggerVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$daggerVersion")

    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1") {
        exclude(group = "org.checkerframework", module = "checker")
    }
}
kapt {
    correctErrorTypes = true
}

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("androidx.navigation.safeargs")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.natiqhaciyef.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 28

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
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    val fragmentVersion = "1.5.2"
    val daggerVersion = "2.46"
    val lifecycleVersion = "2.5.1"
    val viewPagerVersion = "1.0.0"
    val dataStoreVersion = "1.0.0"
    val lottieVersion = "5.2.0"
    val workManagerVersion = "2.8.1"
    val swipeLayoutVersion = "1.1.0"
    val retrofitVersion = "2.9.0"
    val epoxyVersion = "5.1.3"
    val navigationVersion = "2.5.3"

    implementation(project(":common"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

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

    //Reflection
    implementation(kotlin("reflect"))

    //Data store
    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")
    implementation("androidx.security:security-crypto:$dataStoreVersion")

    //Gson
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    //Fragment ktx
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    //View Model Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")


    // Epoxy UI && Masked text
    implementation("com.airbnb.android:epoxy:$epoxyVersion")
    annotationProcessor("com.airbnb.android:epoxy-processor:$epoxyVersion")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-runtime-ktx:$navigationVersion")
}
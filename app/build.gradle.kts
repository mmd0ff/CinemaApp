plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
//    alias(libs.plugins.safe.args.kotlin)
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    alias(libs.plugins.hilt)
//    alias(libs.plugins.google.gms.google.services)
//    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.example.cinemaatl"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cinemaatl"
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
    buildFeatures{
        viewBinding = true
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

    implementation(libs.bundles.navigationLibs)

//    implementation(libs.firebase.crashlytics)
//    implementation(libs.firebase.config)
//    implementation(libs.firebase.firestore)
//    implementation(libs.firebase.auth)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


    implementation(libs.androidx.lifecycle.livedata.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)

//    implementation(libs.coil)

    implementation(libs.bundles.networking)

    implementation(libs.coroutines)

    implementation(libs.swiperefreshlayout)


//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx)
////    implementation(libs.firebase.messaging)
//
//    kapt(libs.room.compiler)

    implementation(libs.hilt)
    kapt(libs.hiltCompiler)

    implementation(libs.media)
//    implementation(libs.workManager)

//    implementation("io.coil-kt.coil3:coil:3.0.0-rc01")
    implementation (libs.glide)
    kapt(libs.glide)

    implementation ("com.github.Dimezis:BlurView:version-2.0.5")
    implementation ("androidx.core:core:1.10.1")




    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
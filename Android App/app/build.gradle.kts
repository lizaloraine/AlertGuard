plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}


android {
    namespace = "com.example.iotbaseddooralarmsystem"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.iotbaseddooralarmsystem"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.analytics)


    // Firebase Realtime Database
    implementation (libs.firebase.database)
    implementation (libs.firebase.messaging)



    // Optional: Other Firebase libraries (if needed)
    implementation (libs.firebase.auth) // For authentication
    implementation (libs.play.services.auth) // Google SignIn
    implementation (libs.firebase.firestore) // If you're using Firestore
    implementation (libs.google.firebase.analytics) // Firebase Analytics
    implementation (libs.material.v190)
}
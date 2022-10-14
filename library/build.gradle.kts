plugins {
    id("com.android.library")
    kotlin("android")
    id("com.flaviofaria.catalog")
}

android {
    namespace = "com.flaviofaria.catalog.library"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
}
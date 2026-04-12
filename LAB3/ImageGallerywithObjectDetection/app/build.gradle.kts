plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.imagegallerywithobjectdetection"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.imagegallerywithobjectdetection"
        minSdk = 24
        targetSdk = 36
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Thư viện phân trang
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    // Thư viện Retrofit để gọi API mạng
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Thư viện Trí tuệ nhân tạo của Google để nhận diện hình ảnh
    implementation("com.google.mlkit:image-labeling:17.0.7")
    // Thư viện Coil để tải hình ảnh từ mạng về mượt mà
    implementation("io.coil-kt:coil:2.5.0")
    // Thư viện quản lý vòng đời ứng dụng
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
}
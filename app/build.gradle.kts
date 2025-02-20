plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.openplatform"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.openplatform"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.core.ktx)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //Compose
    implementation(libs.androidx.ui.v105)
    implementation(libs.ui) //Compose ui 相关的基础支持
    implementation(libs.ui.tooling)//Compose ui 预览
    implementation(libs.foundation)//Compose 基于 ui 层封装的更实用的组件库比如 Scroll、Box 等
    implementation(libs.androidx.material) //Compose Material Design
    implementation(libs.androidx.material.icons.core) //Compose Material design icons
    implementation(libs.androidx.material.icons.extended) //Compose Material design icons
    implementation(libs.androidx.activity.compose)// 将 Activity 支持 Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)//Compose ViewModels 存储数据
    implementation(libs.androidx.runtime.livedata)//Compose 其他集成
    implementation(libs.androidx.runtime.rxjava2)//Compose 其他集成
    implementation(libs.androidx.ui.test.junit4)//Compose UI Tests

    implementation("com.squareup.okhttp3:logging-interceptor:3.9.1")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.github.hannesa2:paho.mqtt.android:4.3")
    implementation("org.greenrobot:eventbus:3.1.1")
    implementation("io.coil-kt:coil-compose:2.4.0")
}
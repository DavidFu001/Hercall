plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.dating.lib"
    compileSdk = 34

    defaultConfig {
        minSdk = Integer.parseInt(properties["BUILD_MIN_SDK_VERSION"].toString())
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    //refer
    implementation("com.android.installreferrer:installreferrer:2.2")

    //Adjust
    implementation("com.adjust.sdk:adjust-android:4.33.5")

    //facebook
    implementation("com.facebook.android:facebook-android-sdk:13.0.0")
    implementation("com.github.yyued:SVGAPlayer-Android:2.6.1")

    //常用依赖库
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.10.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.5.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("androidx.viewpager2:viewpager2:1.0.0")


    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // 基础依赖包，必须要依赖
    implementation("com.geyifeng.immersionbar:immersionbar:3.2.2")
    kapt("androidx.room:room-compiler:2.6.1")

    //Android KTX
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("io.github.scwang90:refresh-layout-kernel:2.0.6")      //核心必须依赖
    implementation("io.github.scwang90:refresh-footer-classics:2.0.6")    //经典加载
    implementation("io.github.scwang90:refresh-header-material:2.1.0")    //谷歌刷新头

    implementation("com.intuit.sdp:sdp-android:1.1.0")//屏幕适配
    implementation("cn.rongcloud.sdk:im_lib:5.6.4") // 融云

    implementation ("com.geyifeng.immersionbar:immersionbar-ktx:3.2.2")
    // 基础依赖包，必须要依赖
    implementation ("com.geyifeng.immersionbar:immersionbar:3.2.2")
}
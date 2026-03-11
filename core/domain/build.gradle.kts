plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
android {
    namespace = "com.alpha.vision.pro.gallery.domain"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions { jvmTarget = "21" }
}
dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation("javax.inject:javax.inject:1")
    implementation(libs.androidx.ui.graphics)
}

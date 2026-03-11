plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}
android {
    namespace = "com.alpha.vision.pro.gallery.designsystem"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions { jvmTarget = "21" }
    buildFeatures { compose = true }
}
dependencies {
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.material3)
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material.icons.extended)
    api(libs.androidx.material3.adaptive)
    debugImplementation(libs.androidx.ui.tooling)
}

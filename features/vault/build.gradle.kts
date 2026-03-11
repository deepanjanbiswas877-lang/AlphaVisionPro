plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}
android {
    namespace  = "com.alpha.vision.pro.gallery.vault"
    compileSdk = 36
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
        freeCompilerArgs += listOf("-opt-in=androidx.compose.material3.ExperimentalMaterial3Api")
    }
    buildFeatures { compose = true }
}
dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.biometric)
}

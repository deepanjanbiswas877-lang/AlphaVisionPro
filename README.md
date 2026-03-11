# Alpha Vision Pro Gallery
> Production-grade Android Gallery & Photo Editor with Native C++ Processing

## Tech Stack
- **Kotlin 2.3.10** + **Jetpack Compose 1.10+**
- **AGP 9.0.1** + **Gradle 9.3.1**
- **Hilt** (DI) + **Room** (Media DB) + **DataStore** (Settings)
- **Native C++ (NDK)** — zero-copy pixel processing via JNI + AndroidBitmap API
- **Material 3** Adaptive Layouts (Foldable + Tablet ready)
- **Coil 3** for image loading + **AndroidX Biometric** for Vault

## Module Structure
```
:app                     → NavGraph, MainActivity, Hilt bootstrap
:core:designsystem       → M3 Theme, Typography, Custom Composables
:core:domain             → UseCases, Models, Repository interfaces
:core:data               → Room DB, MediaStore sync, JNI Bridge, Repos
:features:gallery        → Grid, multi-select, EXIF viewer/stripper
:features:editor         → Native C++ sliders, non-destructive editing
:features:vault          → Biometric-locked hidden folder
:features:settings       → DataStore prefs, GPU toggle, color space
```

## C++ Engine Pipeline
```
Exposure LUT → Shadows/Highlights → Contrast LUT → Saturation (YCbCr) → Warmth → Unsharp Mask
```

## Build
```bash
# 1. Set sdk.dir in local.properties
echo "sdk.dir=$ANDROID_HOME" >> local.properties

# 2. Build debug APK
./gradlew :app:assembleDebug

# 3. Install on device
./gradlew :app:installDebug
```

## NDK Requirements
- NDK version: 27.0+ (r27c recommended)
- ABI filters: arm64-v8a, x86_64
- C++ standard: C++17
- Compiler flags: -O2 -ffast-math

## Package
`com.alpha.vision.pro.gallery`

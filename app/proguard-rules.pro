# Hilt
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel
# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
# JNI
-keepclasseswithmembernames class * {
    native <methods>;
}

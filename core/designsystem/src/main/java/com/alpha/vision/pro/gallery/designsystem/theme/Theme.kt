package com.alpha.vision.pro.gallery.designsystem.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class ColorSpace { SRGB, DISPLAY_P3 }

private val DarkColors = darkColorScheme(
    primary         = PrimaryDark,
    onPrimary       = OnPrimaryDark,
    secondary       = SecondaryDark,
    tertiary        = TertiaryDark,
    background      = BackgroundDark,
    surface         = SurfaceDark,
    surfaceVariant  = SurfaceVariantDark,
    error           = ErrorDark
)

private val LightColors = lightColorScheme(
    primary         = PrimaryLight,
    onPrimary       = OnPrimaryLight,
    secondary       = SecondaryLight,
    tertiary        = TertiaryLight,
    background      = BackgroundLight,
    surface         = SurfaceLight,
    surfaceVariant  = SurfaceVariantLight,
    error           = ErrorLight
)

@Composable
fun AlphaVisionTheme(
    darkTheme    : Boolean    = isSystemInDarkTheme(),
    dynamicColor : Boolean    = true,
    colorSpace   : ColorSpace = ColorSpace.SRGB,
    content      : @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else      -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AlphaTypography,
        content     = content
    )
}

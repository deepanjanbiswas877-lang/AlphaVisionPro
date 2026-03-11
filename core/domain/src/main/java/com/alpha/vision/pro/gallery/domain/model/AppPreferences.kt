package com.alpha.vision.pro.gallery.domain.model

enum class ColorSpace { SRGB, DISPLAY_P3 }

data class AppPreferences(
    val darkMode          : Boolean?   = null,   // null = follow system
    val dynamicColor      : Boolean    = true,
    val colorSpace        : ColorSpace = ColorSpace.SRGB,
    val gpuAcceleration   : Boolean    = true,
    val nativeBufferSizeMb: Int        = 64,
    val autoClearTrashDays: Int        = 30
)

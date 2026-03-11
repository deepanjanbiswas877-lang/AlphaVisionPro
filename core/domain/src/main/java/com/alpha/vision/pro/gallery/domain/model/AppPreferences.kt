package com.alpha.vision.pro.gallery.domain.model

import com.alpha.vision.pro.gallery.designsystem.theme.ColorSpace

data class AppPreferences(
    val darkMode          : Boolean?   = null,   // null = follow system
    val dynamicColor      : Boolean    = true,
    val colorSpace        : ColorSpace = ColorSpace.SRGB,
    val gpuAcceleration   : Boolean    = true,
    val nativeBufferSizeMb: Int        = 64,
    val autoClearTrashDays: Int        = 30
)

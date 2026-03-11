package com.alpha.vision.pro.gallery.domain.model

data class EditParams(
    val exposure   : Float = 0f,   // -1.0 .. +1.0
    val contrast   : Float = 0f,   // -1.0 .. +1.0
    val saturation : Float = 0f,   // -1.0 .. +1.0
    val warmth     : Float = 0f,   // -1.0 .. +1.0
    val highlights : Float = 0f,   // -1.0 .. +1.0
    val shadows    : Float = 0f,   // -1.0 .. +1.0
    val sharpness  : Float = 0f    //  0.0 .. +1.0
)

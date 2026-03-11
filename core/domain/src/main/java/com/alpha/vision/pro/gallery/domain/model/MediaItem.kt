package com.alpha.vision.pro.gallery.domain.model

data class MediaItem(
    val id          : Long,
    val uri         : String,
    val displayName : String,
    val mimeType    : String,
    val dateAdded   : Long,
    val dateModified: Long,
    val width       : Int,
    val height      : Int,
    val size        : Long,
    val bucketName  : String,
    val isVaulted   : Boolean = false,
    val exifData    : ExifData? = null
)

data class ExifData(
    val make          : String? = null,
    val model         : String? = null,
    val focalLength   : String? = null,
    val aperture      : String? = null,
    val shutterSpeed  : String? = null,
    val iso           : String? = null,
    val gpsLatitude   : Double? = null,
    val gpsLongitude  : Double? = null,
    val dateTaken     : String? = null,
    val orientation   : Int     = 0
)

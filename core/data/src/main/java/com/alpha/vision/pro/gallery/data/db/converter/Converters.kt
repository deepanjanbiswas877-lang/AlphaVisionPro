package com.alpha.vision.pro.gallery.data.db.converter

import androidx.room.TypeConverter

class Converters {
    @TypeConverter fun longToString(v: Long?): String? = v?.toString()
    @TypeConverter fun stringToLong(v: String?): Long? = v?.toLongOrNull()
}

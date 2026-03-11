package com.alpha.vision.pro.gallery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alpha.vision.pro.gallery.data.db.converter.Converters
import com.alpha.vision.pro.gallery.data.db.dao.MediaDao
import com.alpha.vision.pro.gallery.data.db.entity.MediaEntity

@Database(
    entities        = [MediaEntity::class],
    version         = 1,
    exportSchema    = true
)
@TypeConverters(Converters::class)
abstract class MediaDatabase : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}

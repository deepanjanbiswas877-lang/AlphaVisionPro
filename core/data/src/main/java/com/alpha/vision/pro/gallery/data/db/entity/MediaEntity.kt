package com.alpha.vision.pro.gallery.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media")
data class MediaEntity(
    @PrimaryKey val id          : Long,
    val uri                     : String,
    val displayName             : String,
    val mimeType                : String,
    val dateAdded               : Long,
    val dateModified            : Long,
    val width                   : Int,
    val height                  : Int,
    val size                    : Long,
    val bucketName              : String,
    val isVaulted               : Boolean = false,
    val isTrashed               : Boolean = false,
    val trashedAt               : Long?   = null
)

package com.alpha.vision.pro.gallery.domain.repository

import android.graphics.Bitmap

interface EditableMediaRepository {
    suspend fun saveEditedBitmap(originalId: Long, bitmap: Bitmap): Result<Long>
}

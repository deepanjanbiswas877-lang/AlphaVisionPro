package com.alpha.vision.pro.gallery.domain.usecase

import android.graphics.Bitmap
import com.alpha.vision.pro.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class SaveEditedMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    // Repository will write to MediaStore and refresh DB entry
    suspend operator fun invoke(originalId: Long, edited: Bitmap): Result<Long> =
        (repository as? com.alpha.vision.pro.gallery.domain.repository.EditableMediaRepository)
            ?.saveEditedBitmap(originalId, edited)
            ?: Result.failure(UnsupportedOperationException("Repository does not support save"))
}

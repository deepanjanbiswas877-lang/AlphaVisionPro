package com.alpha.vision.pro.gallery.domain.usecase

import com.alpha.vision.pro.gallery.domain.model.MediaItem
import com.alpha.vision.pro.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class GetMediaByIdUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(id: Long): Result<MediaItem> = runCatching {
        repository.getMediaById(id) ?: error("Media not found: $id")
    }
}

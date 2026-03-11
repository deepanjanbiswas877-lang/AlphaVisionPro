package com.alpha.vision.pro.gallery.domain.usecase

import com.alpha.vision.pro.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class StripExifUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        repository.stripExif(id)
}

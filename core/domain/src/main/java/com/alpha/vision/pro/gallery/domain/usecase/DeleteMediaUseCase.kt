package com.alpha.vision.pro.gallery.domain.usecase

import com.alpha.vision.pro.gallery.domain.repository.MediaRepository
import javax.inject.Inject

class DeleteMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(ids: List<Long>): Result<Unit> =
        repository.deleteMedia(ids)
}

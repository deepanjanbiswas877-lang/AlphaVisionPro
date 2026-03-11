package com.alpha.vision.pro.gallery.domain.usecase

import com.alpha.vision.pro.gallery.domain.model.MediaItem
import com.alpha.vision.pro.gallery.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTrashUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(): Flow<List<MediaItem>> = repository.observeTrash()
}

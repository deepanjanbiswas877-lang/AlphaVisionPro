package com.alpha.vision.pro.gallery.domain.usecase

import com.alpha.vision.pro.gallery.domain.model.MediaItem
import com.alpha.vision.pro.gallery.domain.repository.VaultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveVaultMediaUseCase @Inject constructor(
    private val vaultRepository: VaultRepository
) {
    operator fun invoke(): Flow<List<MediaItem>> =
        vaultRepository.observeVaultMedia()
}

package com.alpha.vision.pro.gallery.domain.repository

import com.alpha.vision.pro.gallery.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface VaultRepository {
    fun observeVaultMedia(): Flow<List<MediaItem>>
    suspend fun moveToVault(id: Long): Result<Unit>
    suspend fun removeFromVault(id: Long): Result<Unit>
}

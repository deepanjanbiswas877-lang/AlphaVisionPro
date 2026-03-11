package com.alpha.vision.pro.gallery.data.repository

import com.alpha.vision.pro.gallery.data.db.dao.MediaDao
import com.alpha.vision.pro.gallery.data.db.entity.MediaEntity
import com.alpha.vision.pro.gallery.domain.model.MediaItem
import com.alpha.vision.pro.gallery.domain.repository.VaultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaultRepositoryImpl @Inject constructor(
    private val dao: MediaDao
) : VaultRepository {

    override fun observeVaultMedia(): Flow<List<MediaItem>> =
        dao.observeVaultMedia().map { list -> list.map { it.toDomain() } }

    override suspend fun moveToVault(id: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching { dao.setVaulted(id, true) }
        }

    override suspend fun removeFromVault(id: Long): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching { dao.setVaulted(id, false) }
        }

    private fun MediaEntity.toDomain() = MediaItem(
        id = id, uri = uri, displayName = displayName, mimeType = mimeType,
        dateAdded = dateAdded, dateModified = dateModified, width = width,
        height = height, size = size, bucketName = bucketName, isVaulted = isVaulted
    )
}

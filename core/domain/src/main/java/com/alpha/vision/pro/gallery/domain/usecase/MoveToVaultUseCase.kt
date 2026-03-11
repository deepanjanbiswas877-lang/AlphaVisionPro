package com.alpha.vision.pro.gallery.domain.usecase

import com.alpha.vision.pro.gallery.domain.repository.VaultRepository
import javax.inject.Inject

class MoveToVaultUseCase @Inject constructor(
    private val vaultRepository: VaultRepository
) {
    suspend operator fun invoke(id: Long): Result<Unit> =
        vaultRepository.moveToVault(id)
}

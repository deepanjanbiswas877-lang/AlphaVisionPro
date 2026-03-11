package com.alpha.vision.pro.gallery.vault.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.vision.pro.gallery.domain.model.MediaItem
import com.alpha.vision.pro.gallery.domain.usecase.ObserveVaultMediaUseCase
import com.alpha.vision.pro.gallery.domain.repository.VaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VaultUiState(
    val isAuthenticated: Boolean       = false,
    val isAuthenticating: Boolean      = false,
    val mediaItems     : List<MediaItem> = emptyList(),
    val isLoading      : Boolean       = false,
    val error          : String?       = null,
    val snackMessage   : String?       = null
)

sealed interface VaultEvent {
    data object Authenticate        : VaultEvent
    data object AuthSuccess         : VaultEvent
    data class  AuthFailure(val msg: String) : VaultEvent
    data class  RemoveItem(val id: Long)     : VaultEvent
    data object DismissSnack                 : VaultEvent
}

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val observeVaultMedia: ObserveVaultMediaUseCase,
    private val vaultRepository  : VaultRepository
) : ViewModel() {

    private val _state = MutableStateFlow(VaultUiState())
    val state: StateFlow<VaultUiState> = _state.asStateFlow()

    fun onEvent(event: VaultEvent) {
        when (event) {
            is VaultEvent.Authenticate   -> _state.update { it.copy(isAuthenticating = true) }
            is VaultEvent.AuthSuccess    -> {
                _state.update { it.copy(isAuthenticated = true, isAuthenticating = false) }
                loadVaultMedia()
            }
            is VaultEvent.AuthFailure    -> _state.update {
                it.copy(isAuthenticating = false, error = event.msg)
            }
            is VaultEvent.RemoveItem     -> handleRemove(event.id)
            is VaultEvent.DismissSnack   -> _state.update { it.copy(snackMessage = null) }
        }
    }

    private fun loadVaultMedia() = viewModelScope.launch {
        observeVaultMedia()
            .catch { e -> _state.update { it.copy(error = e.message) } }
            .collect { items -> _state.update { it.copy(mediaItems = items, isLoading = false) } }
    }

    private fun handleRemove(id: Long) = viewModelScope.launch {
        vaultRepository.removeFromVault(id)
            .onSuccess { _state.update { it.copy(snackMessage = "Moved back to gallery") } }
            .onFailure { e -> _state.update { it.copy(error = e.message) } }
    }
}

package com.alpha.vision.pro.gallery.gallery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alpha.vision.pro.gallery.domain.model.MediaItem
import com.alpha.vision.pro.gallery.domain.usecase.DeleteMediaUseCase
import com.alpha.vision.pro.gallery.domain.usecase.MoveToVaultUseCase
import com.alpha.vision.pro.gallery.domain.usecase.ObserveAllMediaUseCase
import com.alpha.vision.pro.gallery.domain.usecase.StripExifUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GalleryUiState(
    val mediaItems    : List<MediaItem> = emptyList(),
    val selectedIds   : Set<Long>       = emptySet(),
    val isLoading     : Boolean         = true,
    val error         : String?         = null,
    val gridColumns   : Int             = 3,        // pinch-to-zoom target: 2-5
    val isSelectionMode: Boolean        = false,
    val snackMessage  : String?         = null
)

sealed interface GalleryEvent {
    data class LongPressItem(val id: Long) : GalleryEvent
    data class TapItem(val id: Long)       : GalleryEvent
    data class PinchZoom(val scale: Float) : GalleryEvent
    data object DeleteSelected             : GalleryEvent
    data object MoveSelectedToVault        : GalleryEvent
    data class  StripExif(val id: Long)   : GalleryEvent
    data object ClearSelection             : GalleryEvent
    data object DismissSnack               : GalleryEvent
}

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val observeAllMedia : ObserveAllMediaUseCase,
    private val deleteMedia     : DeleteMediaUseCase,
    private val moveToVault     : MoveToVaultUseCase,
    private val stripExif       : StripExifUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryUiState())
    val state: StateFlow<GalleryUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            observeAllMedia()
                .catch { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
                .collect { items ->
                    _state.update { it.copy(mediaItems = items, isLoading = false) }
                }
        }
    }

    fun onEvent(event: GalleryEvent) {
        when (event) {
            is GalleryEvent.LongPressItem -> handleLongPress(event.id)
            is GalleryEvent.TapItem       -> handleTap(event.id)
            is GalleryEvent.PinchZoom     -> handlePinch(event.scale)
            is GalleryEvent.DeleteSelected-> handleDelete()
            is GalleryEvent.MoveSelectedToVault -> handleVault()
            is GalleryEvent.StripExif     -> handleStripExif(event.id)
            is GalleryEvent.ClearSelection-> _state.update {
                it.copy(selectedIds = emptySet(), isSelectionMode = false)
            }
            is GalleryEvent.DismissSnack  -> _state.update { it.copy(snackMessage = null) }
        }
    }

    private fun handleLongPress(id: Long) {
        _state.update { s ->
            val newSel = s.selectedIds + id
            s.copy(selectedIds = newSel, isSelectionMode = true)
        }
    }

    private fun handleTap(id: Long) {
        val s = _state.value
        if (!s.isSelectionMode) return // handled by NavGraph click
        val newSel = if (id in s.selectedIds) s.selectedIds - id else s.selectedIds + id
        _state.update { it.copy(
            selectedIds     = newSel,
            isSelectionMode = newSel.isNotEmpty()
        )}
    }

    private fun handlePinch(scale: Float) {
        val current = _state.value.gridColumns
        val newCols = when {
            scale < 0.85f -> (current + 1).coerceAtMost(5)
            scale > 1.15f -> (current - 1).coerceAtLeast(2)
            else          -> current
        }
        if (newCols != current) _state.update { it.copy(gridColumns = newCols) }
    }

    private fun handleDelete() = viewModelScope.launch {
        val ids = _state.value.selectedIds.toList()
        deleteMedia(ids)
            .onSuccess {
                _state.update { it.copy(
                    selectedIds    = emptySet(),
                    isSelectionMode= false,
                    snackMessage   = "${ids.size} item(s) moved to trash"
                )}
            }
            .onFailure { e ->
                _state.update { it.copy(error = e.message) }
            }
    }

    private fun handleVault() = viewModelScope.launch {
        _state.value.selectedIds.forEach { id ->
            moveToVault(id)
        }
        _state.update { it.copy(
            selectedIds    = emptySet(),
            isSelectionMode= false,
            snackMessage   = "Items moved to Vault"
        )}
    }

    private fun handleStripExif(id: Long) = viewModelScope.launch {
        stripExif(id)
            .onSuccess { _state.update { it.copy(snackMessage = "EXIF data removed") } }
            .onFailure { e -> _state.update { it.copy(error = e.message) } }
    }
}

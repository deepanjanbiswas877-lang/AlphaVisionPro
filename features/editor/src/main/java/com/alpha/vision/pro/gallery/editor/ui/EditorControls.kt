package com.alpha.vision.pro.gallery.editor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alpha.vision.pro.gallery.designsystem.components.EditorSlider
import com.alpha.vision.pro.gallery.domain.model.EditParams
import com.alpha.vision.pro.gallery.editor.viewmodel.EditorEvent

/**
 * Scrollable column of sliders — each triggers a C++ processing pass via [EditorEvent].
 * Sliders are debounced at 120ms in the ViewModel to avoid hammering the native engine.
 */
@Composable
fun EditorControls(
    params  : EditParams,
    onEvent : (EditorEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        EditorSlider(
            label         = "Exposure",
            value         = params.exposure,
            onValueChange = { onEvent(EditorEvent.SetExposure(it)) },
            valueRange    = -1f..1f
        )
        EditorSlider(
            label         = "Contrast",
            value         = params.contrast,
            onValueChange = { onEvent(EditorEvent.SetContrast(it)) },
            valueRange    = -1f..1f
        )
        EditorSlider(
            label         = "Saturation",
            value         = params.saturation,
            onValueChange = { onEvent(EditorEvent.SetSaturation(it)) },
            valueRange    = -1f..1f
        )
        EditorSlider(
            label         = "Warmth",
            value         = params.warmth,
            onValueChange = { onEvent(EditorEvent.SetWarmth(it)) },
            valueRange    = -1f..1f
        )
        EditorSlider(
            label         = "Highlights",
            value         = params.highlights,
            onValueChange = { onEvent(EditorEvent.SetHighlights(it)) },
            valueRange    = -1f..1f
        )
        EditorSlider(
            label         = "Shadows",
            value         = params.shadows,
            onValueChange = { onEvent(EditorEvent.SetShadows(it)) },
            valueRange    = -1f..1f
        )
        EditorSlider(
            label         = "Sharpness",
            value         = params.sharpness,
            onValueChange = { onEvent(EditorEvent.SetSharpness(it)) },
            valueRange    = 0f..1f
        )
    }
}

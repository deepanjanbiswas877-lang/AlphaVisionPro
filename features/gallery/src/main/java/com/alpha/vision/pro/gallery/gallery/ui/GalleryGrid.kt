package com.alpha.vision.pro.gallery.gallery.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.alpha.vision.pro.gallery.designsystem.components.SelectionOverlay
import com.alpha.vision.pro.gallery.domain.model.MediaItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryGrid(
    items          : List<MediaItem>,
    selectedIds    : Set<Long>,
    columns        : Int,
    onItemClick    : (Long) -> Unit,
    onItemLongPress: (Long) -> Unit,
    onPinchZoom    : (Float) -> Unit,
    onStripExif    : (Long) -> Unit,
    modifier       : Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    var exifTarget by remember { mutableStateOf<MediaItem?>(null) }

    LazyVerticalGrid(
        columns       = GridCells.Fixed(columns),
        modifier      = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    onPinchZoom(zoom)
                }
            },
        contentPadding     = PaddingValues(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement   = Arrangement.spacedBy(2.dp)
    ) {
        items(
            items = items,
            key   = { it.id }
        ) { item ->
            val isSelected = item.id in selectedIds
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .combinedClickable(
                        onClick      = { onItemClick(item.id) },
                        onLongClick  = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onItemLongPress(item.id)
                        }
                    )
                    .animateItem()
            ) {
                AsyncImage(
                    model            = item.uri,
                    contentDescription = item.displayName,
                    contentScale     = ContentScale.Crop,
                    modifier         = Modifier.fillMaxSize()
                )
                SelectionOverlay(
                    selected = isSelected,
                    modifier = Modifier.fillMaxSize()
                )
                // EXIF info button (top-left, always visible)
                IconButton(
                    onClick  = { exifTarget = item },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Filled.Info,
                        contentDescription = "View EXIF",
                        tint               = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier           = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

    exifTarget?.let { item ->
        ExifBottomSheet(
            item        = item,
            onDismiss   = { exifTarget = null },
            onStripExif = {
                onStripExif(item.id)
                exifTarget = null
            }
        )
    }
}

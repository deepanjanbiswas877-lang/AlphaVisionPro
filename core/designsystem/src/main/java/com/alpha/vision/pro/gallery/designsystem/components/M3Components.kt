package com.alpha.vision.pro.gallery.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Animated selection overlay for gallery items. */
@Composable
fun SelectionOverlay(
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val overlayColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                      else Color.Transparent,
        animationSpec = tween(150),
        label = "selectionOverlay"
    )
    Box(modifier = modifier.background(overlayColor)) {
        if (selected) {
            Icon(
                imageVector        = Icons.Filled.CheckCircle,
                contentDescription = "Selected",
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(24.dp)
            )
        }
    }
}

/** Pill-shaped chip for EXIF tags. */
@Composable
fun ExifChip(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(50),
        color    = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(text = value, style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer)
        }
    }
}

/** Full-width labeled slider for editor controls. */
@Composable
fun EditorSlider(
    label    : String,
    value    : Float,
    onValueChange : (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = -1f..1f,
    modifier : Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = label, style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = "%.2f".format(value),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value          = value,
            onValueChange  = onValueChange,
            valueRange     = valueRange,
            modifier       = Modifier.fillMaxWidth(),
            colors         = SliderDefaults.colors(
                thumbColor        = MaterialTheme.colorScheme.primary,
                activeTrackColor  = MaterialTheme.colorScheme.primary,
                inactiveTrackColor= MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

/** Bottom sheet drag handle. */
@Composable
fun DragHandle(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(vertical = 8.dp)
            .size(width = 32.dp, height = 4.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
    )
}

/** Top app bar for feature screens. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlphaTopBar(
    title         : String,
    navigationIcon: @Composable () -> Unit = {},
    actions       : @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title             = { Text(text = title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon    = navigationIcon,
        actions           = actions,
        scrollBehavior    = scrollBehavior,
        colors            = TopAppBarDefaults.topAppBarColors(
            containerColor         = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

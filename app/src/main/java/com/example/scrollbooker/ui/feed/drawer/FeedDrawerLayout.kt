package com.example.scrollbooker.ui.feed.drawer

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FeedDrawerLayout(
    isOpen: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    scrimColor: Color = Color.Black.copy(alpha = 0.7f),
    drawerContent: @Composable ColumnScope.() -> Unit,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .zIndex(15f)
    ) {
        val fullWidthPx = with(density) { maxWidth.toPx() }
        val drawerWidthPx = fullWidthPx * 0.9f
        val closedOffsetX = -drawerWidthPx

        val offsetX = remember { Animatable(if (isOpen) 0f else closedOffsetX) }

        LaunchedEffect(isOpen, drawerWidthPx) {
            val target = if (isOpen) 0f else closedOffsetX
            if (offsetX.targetValue != target) {
                offsetX.animateTo(target)
            }
        }

        val openProgress = ((offsetX.value - closedOffsetX) / (0f - closedOffsetX)).coerceIn(0f, 1f)

        if (openProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(9f)
                    .graphicsLayer { alpha = openProgress }
                    .background(scrimColor)
                    .pointerInput(Unit) {
                        detectTapGestures { onOpenChange(false) }
                    }
            )
        }

        Box(
            modifier = Modifier
                .zIndex(10f)
                .fillMaxHeight()
                .width(with(density) { drawerWidthPx.toDp() })
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val newValue = (offsetX.value + delta).coerceIn(closedOffsetX, 0f)
                            offsetX.snapTo(newValue)
                        }
                    },
                    onDragStopped = { velocity ->
                        val shouldOpen = when {
                            velocity > 800f -> true
                            velocity < -800f -> false
                            else -> openProgress > 0.5f
                        }

                        onOpenChange(shouldOpen)
                    }
                )
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .safeDrawingPadding()
            ) {
                drawerContent()
            }
        }
    }
}
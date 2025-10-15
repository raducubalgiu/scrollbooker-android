package com.example.scrollbooker.ui.shared.posts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Primary
import kotlin.math.roundToInt

@Composable
fun VideoSlider(
    modifier: Modifier,
    progress: Float,
    isPlaying: Boolean,
    onSeek: (Float) -> Unit
) {
    var barWidth by remember { mutableFloatStateOf(0f) }

    val thumbOffsetPx = remember(barWidth, progress) {
        (barWidth * progress).coerceIn(0f, barWidth)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(if(isPlaying) 3.dp else 8.dp)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDrag = { change, dragAmount ->
                        val newX = (thumbOffsetPx + dragAmount.x)
                            .coerceIn(0f, barWidth)
                        onSeek(newX / barWidth)
                    }
                )
            }
            .onSizeChanged { size ->
                barWidth = size.width.toFloat()
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.3f))
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(with(LocalDensity.current) { (barWidth * progress).toDp() })
                .align(Alignment.CenterStart)
                .background(Primary.copy(alpha = 0.3f))
        )
        if(!isPlaying) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(thumbOffsetPx.roundToInt(), 0) }
                    .size(8.dp)
                    .background(Primary.copy(alpha = 0.3f))
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(Primary)
            )
        }
    }
}
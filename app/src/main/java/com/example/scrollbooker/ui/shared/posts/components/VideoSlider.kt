package com.example.scrollbooker.ui.shared.posts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.scrollbooker.core.util.Dimens.SpacingS
import kotlin.math.roundToInt

@Composable
fun VideoSlider(
    modifier: Modifier,
    progress: Float,
    isPlaying: Boolean,
    onSeek: (Float) -> Unit
) {
    var barWidth by rememberSaveable { mutableFloatStateOf(0f) }

    val thumbOffsetPx = remember(barWidth, progress) {
        (barWidth * progress).coerceIn(0f, barWidth)
    }

    val density = LocalDensity.current
    val progressWidth = with(density) { (barWidth * progress).toDp() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingS)
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
            .onSizeChanged { size -> barWidth = size.width.toFloat() }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray.copy(alpha = 0.5f))
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(progressWidth)
                .align(Alignment.CenterStart)
                .background(Color.White)
        )

        if(!isPlaying) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(thumbOffsetPx.roundToInt(), 0) }
                    .size(8.dp)
                    .align(Alignment.CenterStart)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}
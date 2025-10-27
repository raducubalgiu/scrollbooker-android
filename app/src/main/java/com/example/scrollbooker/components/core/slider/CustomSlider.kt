package com.example.scrollbooker.components.core.slider
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    trackHeight: Dp = 8.dp,
    thumbRadius: Dp = 10.dp,
    onDragEnd: (() -> Unit)? = null
) {
    var widthPx by remember { mutableFloatStateOf(1f) }
    var isDragging by remember { mutableStateOf(false) }

    val animatedValue by animateFloatAsState(
        targetValue = value.coerceIn(valueRange.start, valueRange.endInclusive),
        label = "sliderAnim"
    )

    val activeColor = Primary
    val inactiveColor = Divider

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbRadius * 2)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        isDragging = false
                        onDragEnd?.invoke()
                    },
                    onDrag = { change, _ ->
                        change.consume()
                        val newValue = (change.position.x / widthPx)
                            .coerceIn(0f, 1f)
                            .let { it * (valueRange.endInclusive - valueRange.start) + valueRange.start }
                        onValueChange(newValue)
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val newValue = (tapOffset.x / widthPx)
                        .coerceIn(0f, 1f)
                        .let { it * (valueRange.endInclusive - valueRange.start) + valueRange.start }
                    onValueChange(newValue)
                }
            }
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { widthPx = it.size.width.toFloat() }
        ) {
            val progress =
                ((animatedValue - valueRange.start) / (valueRange.endInclusive - valueRange.start))
                    .coerceIn(0f, 1f)
            val trackY = size.height / 2
            val activeWidth = size.width * progress

            drawLine(
                color = inactiveColor,
                start = Offset(0f, trackY),
                end = Offset(size.width, trackY),
                strokeWidth = trackHeight.toPx(),
                cap = StrokeCap.Round
            )

            drawLine(
                color = activeColor,
                start = Offset(0f, trackY),
                end = Offset(activeWidth, trackY),
                strokeWidth = trackHeight.toPx(),
                cap = StrokeCap.Round
            )

            drawCircle(
                color = activeColor,
                radius = thumbRadius.toPx(),
                center = Offset(activeWidth, trackY)
            )
        }
    }
}
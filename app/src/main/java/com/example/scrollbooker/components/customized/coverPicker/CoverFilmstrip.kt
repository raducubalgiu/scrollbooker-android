package com.example.scrollbooker.components.customized.coverPicker

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import kotlin.math.roundToInt

/**
 * Horizontal filmstrip + draggable scrub handle used by any "pick a cover frame from a
 * video" screen (local file or remote stream alike) - the frames themselves are just
 * bitmaps, so this has no opinion on where they came from.
 */
@Composable
fun CoverFilmstrip(
    frames: List<Bitmap>,
    durationMs: Long,
    positionMs: Long,
    onScrub: (Long) -> Unit
) {
    var widthPx by remember { mutableFloatStateOf(1f) }
    val handleWidth = 4.dp
    val stripShape = RoundedCornerShape(SpacingS)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = BasePadding)
            .onGloballyPositioned { widthPx = it.size.width.toFloat() }
            .pointerInput(durationMs) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val fraction = (change.position.x / widthPx).coerceIn(0f, 1f)
                    onScrub((fraction * durationMs).toLong())
                }
            }
            .pointerInput(durationMs) {
                detectTapGestures { tapOffset ->
                    val fraction = (tapOffset.x / widthPx).coerceIn(0f, 1f)
                    onScrub((fraction * durationMs).toLong())
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(stripShape)
                .border(1.dp, Color.White.copy(alpha = 0.15f), stripShape),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            frames.forEach { frame ->
                Image(
                    bitmap = frame.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        val fraction = (positionMs.toFloat() / durationMs).coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .offset {
                    val handleWidthPx = handleWidth.toPx()
                    IntOffset(((widthPx - handleWidthPx) * fraction).roundToInt(), 0)
                }
                .width(handleWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(3.dp))
                .background(Color.White)
                .border(1.dp, Color.Black.copy(alpha = 0.35f), RoundedCornerShape(3.dp))
        )
    }
}

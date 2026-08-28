package com.example.scrollbooker.components.customized.post.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.labelLarge
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToLong

private const val ProgressPollIntervalMs = 200L
private const val MinSeekIntervalMs = 80L
private const val ShortVideoDurationThresholdMs = 30_000L
private val IdleTrackHeight = 2.dp
private val ActiveTrackHeight = 4.dp
private val IdleThumbRadius = 0.dp
private val ActiveThumbRadius = 6.dp
private val TouchTargetHeight = 36.dp
private val TrackBottomInset = 2.dp

@Composable
fun VideoScrubber(
    player: ExoPlayer,
    isFocused: Boolean,
    isPaused: Boolean,
    onSeekingChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remember(player) { mutableFloatStateOf(0f) }
    val currentPositionMs = remember(player) { mutableLongStateOf(0L) }
    val durationMs = remember(player) { mutableLongStateOf(0L) }

    var isDragging by remember(player) { mutableStateOf(false) }
    var dragFraction by remember(player) { mutableFloatStateOf(0f) }
    var lastSeekAtMs by remember(player) { mutableLongStateOf(0L) }

    val currentOnSeekingChanged = rememberUpdatedState(onSeekingChanged)

    LaunchedEffect(player, isFocused) {
        if (!isFocused) return@LaunchedEffect
        while (isActive) {
            if (!isDragging) {
                val duration = player.duration
                if (duration > 0) {
                    durationMs.longValue = duration
                    val position = player.currentPosition
                    currentPositionMs.longValue = position
                    progress.floatValue = (position.toFloat() / duration).coerceIn(0f, 1f)
                }
            }
            delay(ProgressPollIntervalMs)
        }
    }

    fun fractionAt(x: Float, widthPx: Int): Float =
        if (widthPx <= 0) 0f else (x / widthPx).coerceIn(0f, 1f)

    fun applyDrag(fraction: Float, forceSeek: Boolean) {
        dragFraction = fraction
        val duration = durationMs.longValue
        if (duration <= 0) return
        currentPositionMs.longValue = (fraction * duration).roundToLong()

        val now = System.currentTimeMillis()
        if (forceSeek || now - lastSeekAtMs >= MinSeekIntervalMs) {
            lastSeekAtMs = now
            player.seekTo(currentPositionMs.longValue)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingM)
    ) {
        AnimatedVisibility(
            visible = isDragging,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = TouchTargetHeight),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            VideoTimeLabel(
                currentTimeMs = currentPositionMs.longValue,
                totalTimeMs = durationMs.longValue
            )
        }

        val isScrubberActive = isDragging || isPaused
        val isShortVideo = durationMs.longValue in 1 until ShortVideoDurationThresholdMs
        val isTrackVisible = isScrubberActive || !isShortVideo

        val trackHeight by animateDpAsState(
            targetValue = if (isScrubberActive) ActiveTrackHeight else IdleTrackHeight,
            label = "scrubberTrackHeight"
        )
        val thumbRadius by animateDpAsState(
            targetValue = if (isScrubberActive) ActiveThumbRadius else IdleThumbRadius,
            label = "scrubberThumbRadius"
        )
        val trackAlpha by animateFloatAsState(
            targetValue = if (isTrackVisible) 1f else 0f,
            label = "scrubberVisibility"
        )

        Canvas(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(TouchTargetHeight)
                .pointerInput(player) {
                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val pointerId = down.id
                        val downX = down.position.x
                        val touchSlop = viewConfiguration.touchSlop

                        var change: PointerInputChange
                        while (true) {
                            val event = awaitPointerEvent()
                            change = event.changes.firstOrNull { it.id == pointerId } ?: return@awaitEachGesture
                            if (!change.pressed) return@awaitEachGesture
                            if (abs(change.position.x - downX) >= touchSlop) break
                        }

                        change.consume()
                        isDragging = true
                        currentOnSeekingChanged.value(true)
                        applyDrag(fractionAt(change.position.x, size.width), forceSeek = true)

                        while (true) {
                            val event = awaitPointerEvent()
                            val nextChange = event.changes.firstOrNull { it.id == pointerId } ?: break

                            if (!nextChange.pressed) {
                                nextChange.consume()
                                break
                            }

                            nextChange.consume()
                            applyDrag(fractionAt(nextChange.position.x, size.width), forceSeek = false)
                        }

                        isDragging = false
                        currentOnSeekingChanged.value(false)
                        player.seekTo(currentPositionMs.longValue)
                    }
                }
        ) {
            val fraction = if (isDragging) dragFraction else progress.floatValue
            val trackStrokePx = trackHeight.toPx()
            val y = size.height - TrackBottomInset.toPx()

            drawLine(
                color = Color.White.copy(alpha = 0.35f * trackAlpha),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = trackStrokePx,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White.copy(alpha = trackAlpha),
                start = Offset(0f, y),
                end = Offset(size.width * fraction, y),
                strokeWidth = trackStrokePx,
                cap = StrokeCap.Round
            )
            if (thumbRadius > 0.dp) {
                drawCircle(
                    color = Color.White.copy(alpha = trackAlpha),
                    radius = thumbRadius.toPx(),
                    center = Offset(size.width * fraction, y)
                )
            }
        }
    }
}

@Composable
private fun VideoTimeLabel(currentTimeMs: Long, totalTimeMs: Long) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = "${formatTime(currentTimeMs)} / ${formatTime(totalTimeMs)}",
            style = labelLarge,
            color = Color.White
        )
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000L).coerceAtLeast(0L)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format(Locale.US, "%d:%02d", minutes, seconds)
}

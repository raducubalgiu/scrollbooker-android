package com.example.scrollbooker.ui.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private const val PREVIEW_SETTLE_DELAY_MS = 150L

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun CreatePostCoverScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.cameraVideoUiState.collectAsState()
    val player by viewModel.player.collectAsState()
    val filmstrip by viewModel.filmstrip.collectAsState()

    val durationMs = player?.duration?.takeIf { it > 0 } ?: 0L
    var selectedPositionMs by remember(state.selectedUri) {
        mutableLongStateOf((state.coverTimeUs ?: 0L) / 1_000L)
    }

    LaunchedEffect(state.selectedUri, durationMs) {
        if (durationMs > 0) viewModel.ensureFilmstrip(durationMs)
    }

    LaunchedEffect(Unit) {
        viewModel.pause()
    }

    val filmstripFrame = remember(filmstrip, selectedPositionMs, durationMs) {
        if (filmstrip.isEmpty() || durationMs <= 0L) return@remember null

        val fraction = (selectedPositionMs.toFloat() / durationMs).coerceIn(0f, 0.999f)
        val index = (fraction * filmstrip.size).toInt().coerceIn(0, filmstrip.size - 1)
        filmstrip[index]
    }

    var sharpFrame by remember(state.selectedUri) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(selectedPositionMs, durationMs) {
        if (durationMs <= 0L) return@LaunchedEffect
        sharpFrame = null
        delay(PREVIEW_SETTLE_DELAY_MS)
        sharpFrame = viewModel.extractPreviewFrame(selectedPositionMs * 1_000L)
    }

    val previewFrame = sharpFrame ?: filmstripFrame

    Scaffold(containerColor = BackgroundDark) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpacingS),
                contentAlignment = Alignment.CenterStart
            ) {
                CustomIconButton(
                    imageVector = Icons.Default.Close,
                    boxSize = 50.dp,
                    iconSize = 26.dp,
                    tint = Color.White,
                    onClick = onBack
                )

                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(R.string.chooseCover),
                    style = bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = BasePadding),
                contentAlignment = Alignment.Center
            ) {
                previewFrame?.let { frame ->
                    Image(
                        bitmap = frame.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(BasePadding)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(Modifier.height(SpacingXL))

            if (filmstrip.isNotEmpty() && durationMs > 0) {
                CoverFilmstrip(
                    frames = filmstrip,
                    durationMs = durationMs,
                    positionMs = selectedPositionMs,
                    onScrub = { selectedPositionMs = it }
                )
            }

            Spacer(Modifier.height(SpacingXL))

            Box(Modifier.padding(horizontal = BasePadding, vertical = SpacingS)) {
                MainButton(
                    modifier = Modifier.navigationBarsPadding(),
                    onClick = {
                        viewModel.setCoverAtTimestamp(selectedPositionMs * 1_000L)
                        onBack()
                    },
                    title = stringResource(R.string.selectCover),
                    contentPadding = PaddingValues(SpacingM)
                )
            }
        }
    }
}

@Composable
private fun CoverFilmstrip(
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

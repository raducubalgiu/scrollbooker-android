package com.example.scrollbooker.ui.editPost

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.TextureView
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.customized.coverPicker.CoverFilmstrip
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.bodyLarge
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

private const val EDIT_FILMSTRIP_FRAME_COUNT = 10
private const val EDIT_PREVIEW_SETTLE_DELAY_MS = 200L
private const val SEEK_TIMEOUT_MS = 3_000L
private const val SEEK_SETTLE_DELAY_MS = 120L

/**
 * Cover picker for a post that's already been published: the video only exists on
 * Cloudflare Stream (HLS), not as a local file, so frames are captured by seeking a real
 * player and snapshotting its rendered surface - MediaMetadataRetriever (used by the
 * create-post picker) does not reliably support HLS sources.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun EditPostCoverScreen(
    viewModel: EditPostViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val player by viewModel.player.collectAsState()
    val isPlayerReady by viewModel.isPlayerReady.collectAsState()
    val filmstrip by viewModel.filmstrip.collectAsState()
    val pendingCoverTimeUs by viewModel.pendingCoverTimeUs.collectAsState()

    val durationMs = if (isPlayerReady) player?.duration?.takeIf { it > 0 } ?: 0L else 0L

    var selectedPositionMs by remember {
        mutableLongStateOf((pendingCoverTimeUs ?: 0L) / 1_000L)
    }
    var sharpFrame by remember { mutableStateOf<Bitmap?>(null) }
    var isGeneratingFilmstrip by remember { mutableStateOf(false) }

    val textureView = remember {
        TextureView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    suspend fun Player.seekAndAwaitFrame(positionMs: Long) {
        val deferred = CompletableDeferred<Unit>()
        val listener = object : Player.Listener {
            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                if (!deferred.isCompleted) deferred.complete(Unit)
            }
        }
        addListener(listener)
        seekTo(positionMs)
        withTimeoutOrNull(SEEK_TIMEOUT_MS) { deferred.await() }
        removeListener(listener)
        delay(SEEK_SETTLE_DELAY_MS)
    }

    LaunchedEffect(Unit) {
        viewModel.ensurePlayerPrepared()
    }

    DisposableEffect(player) {
        player?.setVideoTextureView(textureView)
        onDispose { player?.clearVideoTextureView(textureView) }
    }

    // Single coroutine owns every seek on this player: generates the filmstrip once (if not
    // already cached), then watches the scrub position and captures a sharp frame whenever
    // it settles. Keeping both in one place rules out two seeks racing each other.
    LaunchedEffect(player, durationMs) {
        val p = player ?: return@LaunchedEffect
        if (durationMs <= 0L) return@LaunchedEffect

        if (filmstrip.isEmpty()) {
            isGeneratingFilmstrip = true
            val frames = mutableListOf<Bitmap>()
            val interval = durationMs / EDIT_FILMSTRIP_FRAME_COUNT

            for (index in 0 until EDIT_FILMSTRIP_FRAME_COUNT) {
                val targetMs = interval * index + interval / 2
                p.seekAndAwaitFrame(targetMs)
                textureView.bitmap?.let { frames.add(it) }
            }

            viewModel.setFilmstrip(frames)
            isGeneratingFilmstrip = false
        }

        snapshotFlow { selectedPositionMs }
            .collectLatest { positionMs ->
                sharpFrame = null
                delay(EDIT_PREVIEW_SETTLE_DELAY_MS)
                p.seekAndAwaitFrame(positionMs)
                sharpFrame = textureView.bitmap
            }
    }

    val filmstripFrame = remember(filmstrip, selectedPositionMs, durationMs) {
        if (filmstrip.isEmpty() || durationMs <= 0L) return@remember null
        val fraction = (selectedPositionMs.toFloat() / durationMs).coerceIn(0f, 0.999f)
        val index = (fraction * filmstrip.size).toInt().coerceIn(0, filmstrip.size - 1)
        filmstrip[index]
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
                // Invisible decode target frames are captured from - the visible UI is always
                // the last captured bitmap, so scrubbing never shows raw, half-seeked output.
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0f),
                    factory = { textureView }
                )

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

                if (isGeneratingFilmstrip && previewFrame == null) {
                    CircularProgressIndicator(color = Color.White)
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
                    enabled = !isGeneratingFilmstrip,
                    onClick = {
                        scope.launch {
                            val finalFrame = sharpFrame ?: run {
                                player?.seekAndAwaitFrame(selectedPositionMs)
                                textureView.bitmap
                            }
                            finalFrame?.let {
                                viewModel.setPendingCover(it, selectedPositionMs * 1_000L)
                            }
                            onBack()
                        }
                    },
                    title = stringResource(R.string.selectCover),
                    contentPadding = PaddingValues(SpacingM)
                )
            }
        }
    }
}

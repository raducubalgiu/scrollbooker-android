package com.example.scrollbooker.ui.camera
import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlin.math.max

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(UnstableApi::class)
@Composable
fun CameraPreviewScreen(
    viewModel: CameraViewModel,
    onBack: () -> Unit,
    onNavigateToCreatePostScreen: () -> Unit
) {
    val player by viewModel.player.collectAsState()
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(state.selectedUri) {
        viewModel.generateCoverIfNeeded()
    }

    val playerView = remember {
        PlayerView(context).apply {
            useController = false
            controllerAutoShow = false
            controllerShowTimeoutMs = 0
            setShowBuffering(SHOW_BUFFERING_NEVER)

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
    }

    DisposableEffect(player) {
        playerView.player = player
        onDispose { playerView.player = null }
    }

    DisposableEffect(player) {
        val p = player ?: return@DisposableEffect onDispose {  }

        val listener = object : Player.Listener {
            override fun onVideoSizeChanged(videoSize: VideoSize) {
                val videoRatio =
                    (videoSize.width * videoSize.pixelWidthHeightRatio) /
                            max(1, videoSize.height).toFloat()

                playerView.resizeMode =
                    if (videoRatio >= 1f) {
                        AspectRatioFrameLayout.RESIZE_MODE_FIT
                    } else {
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    }
            }
        }
        p.addListener(listener)
        onDispose { p.removeListener(listener) }
    }

    LifecycleStartEffect(Unit) {
        viewModel.play()

        onStopOrDispose {
            viewModel.pause()
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {
            Box(modifier = Modifier.padding(BasePadding)) {
                MainButton(
                    modifier = Modifier.padding(bottom = BasePadding),
                    onClick = onNavigateToCreatePostScreen,
                    title = stringResource(R.string.nextStep),
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            CustomIconButton(
                imageVector = Icons.Default.Close,
                boxSize = 60.dp,
                iconSize = 30.dp,
                tint = Color.White,
                onClick = {
                    viewModel.onBackToGallery()
                    onBack()
                }
            )

            if(state.isReady && player != null) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(BasePadding))
                        .background(BackgroundDark.copy(alpha = 0.6f)),
                    factory = { playerView },
                    update = { playerView },
                )
            }
        }
    }
}
package com.example.scrollbooker.modules.posts.videoItem
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.scrollbooker.modules.posts.common.VideoViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(UnstableApi::class)
@Composable
fun PostVideo(
    url: String?,
    playWhenReady: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val viewModel: VideoViewModel = hiltViewModel()

    val latestGlobalPlayState by rememberUpdatedState(playWhenReady)
    var isPlayerReady by remember { mutableStateOf(false) }
    var isLocallyPlaying by remember { mutableStateOf(playWhenReady) }

    // Slider
    //var currentPosition by remember { mutableLongStateOf(0L) }
    //var duration by remember { mutableLongStateOf(1L) }
    var isUserDragging by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val exoPlayer = remember(url) { viewModel.getOrCreatePlayer(url ?: "") }

    LaunchedEffect(latestGlobalPlayState) {
        isLocallyPlaying = latestGlobalPlayState
        exoPlayer.playWhenReady = latestGlobalPlayState
    }

    Box(modifier = modifier
        .fillMaxWidth()
        .pointerInput(Unit) {
            detectTapGestures {
                isLocallyPlaying = !isLocallyPlaying
                exoPlayer.playWhenReady = isLocallyPlaying
            }
        }
    ) {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    controllerShowTimeoutMs = 0
                    controllerHideOnTouch = false
                    controllerAutoShow = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    hideController()
                }
            },
            update = {
                if(it.player != exoPlayer) {
                    it.player = exoPlayer
                }
            }
        )
    }

    DisposableEffect(Unit) {
//        val listener = object: Player.Listener {
//            override fun onPlaybackStateChanged(state: Int) {
//                if (state == Player.STATE_READY) {
//                    isPlayerReady = true
//                }
//            }
//        }
//
//        val job = scope.launch {
//            while (isActive) {
//                if(exoPlayer.isPlaying && !isUserDragging) {
//                    currentPosition = exoPlayer.currentPosition
//                    duration = exoPlayer.duration.coerceAtLeast(1L)
//                }
//                delay(500L)
//            }
//        }
//
//        exoPlayer.addListener(listener)

        onDispose {
            //exoPlayer.removeListener(listener)
            //job.cancel()
            //exoPlayer.release()
        }
    }
}
package com.example.scrollbooker.screens.feed.components
import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.shared.post.domain.model.Post
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(UnstableApi::class)
@Composable
fun PostVideoItem(
    post: Post,
    videoHeight: Dp,
    playWhenReady: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val url = post.mediaFiles.first().url

    val latestGlobalPlayState by rememberUpdatedState(playWhenReady)

    val exoPlayer = remember(url) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            repeatMode = Player.REPEAT_MODE_ALL
            volume = 1f
            prepare()
        }
    }

    var isPlayerReady by remember { mutableStateOf(false) }

    var isLocallyPlaying by remember { mutableStateOf(playWhenReady) }

    // Slider
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(1L) }
    var isUserDragging by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

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
        if(isPlayerReady) {
            AndroidView(
                modifier = modifier
                    .fillMaxWidth()
                    .height(videoHeight),
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                }
            )

//            if(!isLocallyPlaying) {
//                Box(modifier = Modifier.fillMaxSize(),
//                    contentAlignment = Alignment.BottomCenter
//                ) {
//                    Column {
//                        Slider(
//                            modifier = Modifier
//                                .height(2.dp)
//                                .padding(horizontal = BasePadding),
//                            value = currentPosition.toFloat(),
//                            valueRange = 0f..duration.toFloat(),
//                            onValueChange = { newValue ->
//                                currentPosition = newValue.toLong()
//                                isUserDragging = false
//                            },
//                            onValueChangeFinished = {
//                                exoPlayer.seekTo(currentPosition)
//                                isUserDragging = false
//                            },
//                            colors = SliderColors(
//                                thumbColor = Error,
//                                activeTrackColor = Primary,
//                                activeTickColor = Color.Yellow,
//                                inactiveTrackColor = SurfaceBG,
//                                inactiveTickColor = SurfaceBG,
//                                disabledThumbColor = Divider,
//                                disabledActiveTrackColor = Divider,
//                                disabledActiveTickColor = Divider,
//                                disabledInactiveTrackColor = Divider,
//                                disabledInactiveTickColor = Divider
//                            )
//                        )
//                    }
//                }
//            }
        }
    }

    DisposableEffect(Unit) {
        val listener = object: Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    isPlayerReady = true
                }
            }
        }

        val job = scope.launch {
            while (isActive) {
                if(exoPlayer.isPlaying && !isUserDragging) {
                    currentPosition = exoPlayer.currentPosition
                    duration = exoPlayer.duration.coerceAtLeast(1L)
                }
                delay(500L)
            }
        }

        exoPlayer.addListener(listener)

        onDispose {
            exoPlayer.removeListener(listener)
            job.cancel()
            exoPlayer.release()
        }
    }
}
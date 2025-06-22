package com.example.scrollbooker.screens.feed.components
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.scrollbooker.shared.post.domain.model.Post

@OptIn(UnstableApi::class)
@Composable
fun PostVideoItem(
    post: Post,
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

    var isLocallyPlaying by remember { mutableStateOf(playWhenReady) }

    LaunchedEffect(latestGlobalPlayState) {
        isLocallyPlaying = latestGlobalPlayState
        exoPlayer.playWhenReady = latestGlobalPlayState
    }

    Box(modifier = modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures {
                isLocallyPlaying = !isLocallyPlaying
                exoPlayer.playWhenReady = isLocallyPlaying
            }
        }
    ) {
        AndroidView(
            modifier = modifier.fillMaxSize(),
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
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}
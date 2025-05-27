package com.example.scrollbooker.feature.feed.presentation.tabs

import android.net.Uri
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.scrollbooker.R

@OptIn(UnstableApi::class)
@Composable
fun BookNowTab(shouldVideoPlay: Boolean) {
    val context = LocalContext.current
    val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.haircut}")

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            this.playWhenReady = shouldVideoPlay
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    LaunchedEffect(shouldVideoPlay) {
        exoPlayer.playWhenReady = shouldVideoPlay
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
                controllerAutoShow = false
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        modifier = Modifier.fillMaxSize().background(Color.Black)
    )

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }
}
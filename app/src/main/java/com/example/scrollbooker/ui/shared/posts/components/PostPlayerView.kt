package com.example.scrollbooker.ui.shared.posts.components

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER

@OptIn(UnstableApi::class)
@Composable
fun PostPlayerView(player: ExoPlayer) {
    AndroidView(
        factory = { context ->
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
        },
        update = { playerView ->
            playerView.player = player
            playerView.resizeMode =
                AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        },
        modifier = Modifier.fillMaxSize(),
    )
}
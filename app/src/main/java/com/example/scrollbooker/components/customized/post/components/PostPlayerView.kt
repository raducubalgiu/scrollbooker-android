package com.example.scrollbooker.components.customized.post.components

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER
import coil.compose.AsyncImage

@OptIn(UnstableApi::class)
@Composable
fun PostPlayerView(
    player: ExoPlayer,
    thumbnailUrl: String
) {
    var isVideoRendered by remember(player) { mutableStateOf(false) }
    val context = LocalContext.current

    val playerView = remember {
        PlayerView(context).apply {
            useController = false
            controllerAutoShow = false
            controllerShowTimeoutMs = 0

            setKeepContentOnPlayerReset(true)
            setShowBuffering(SHOW_BUFFERING_NEVER)

            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        }
    }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                isVideoRendered = true
            }
        }
        player.addListener(listener)
        playerView.player = player

        onDispose {
            player.removeListener(listener)
            playerView.player = null
        }
    }

    DisposableEffect(player) {
        playerView.player = player
        onDispose {
            playerView.player = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { playerView },
            update = { view ->
                if (view.player != player) view.player = player
                view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            },
            modifier = Modifier.fillMaxSize(),
        )

        if (!isVideoRendered) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = thumbnailUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}
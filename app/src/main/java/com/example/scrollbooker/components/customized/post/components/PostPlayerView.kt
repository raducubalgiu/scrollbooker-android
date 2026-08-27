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
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_NEVER
import coil.compose.AsyncImage

@OptIn(UnstableApi::class)
@Composable
fun PostPlayerView(
    player: Player,
    displayThumbnail: Boolean,
    thumbnailUrl: String,
) {
    // A fresh post is seekTo(0)'d before being prepared, so position stays 0 until
    // real playback progress happens — that takes longer than a single Compose
    // frame, so position > 0 here reliably means this player is being reattached
    // to a new surface mid-playback (e.g. after navigating away and back), not
    // starting cold. Skipping the thumbnail in that case avoids a stale-frame
    // flash; keeping it for a genuinely fresh player avoids a black screen while
    // the video is still loading.
    var isVideoRendered by remember(player, player.currentMediaItem) {
        mutableStateOf(player.currentPosition > 0)
    }
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


    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { playerView },
            update = { view ->
                if (view.player != player) view.player = player
                view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            },
            modifier = Modifier.fillMaxSize(),
        )

        if (!isVideoRendered && displayThumbnail) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = thumbnailUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}
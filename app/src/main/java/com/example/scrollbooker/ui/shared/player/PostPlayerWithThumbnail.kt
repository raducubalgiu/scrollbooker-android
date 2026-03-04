package com.example.scrollbooker.ui.shared.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.shared.posts.components.PostPlayerView

@Composable
fun PostPlayerWithThumbnail(
    player: ExoPlayer,
    thumbnailUrl: String,
    showPlayIcon: Boolean = false
) {
    var isRenderedFirstFrame by remember { mutableStateOf(false) }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                isRenderedFirstFrame = true
            }
        }
        player.addListener(listener)
        onDispose { player.removeListener(listener) }
    }

    Box(Modifier.fillMaxSize()) {
        PostPlayerView(player)

        if(!isRenderedFirstFrame) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = thumbnailUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        AnimatedVisibility(
            visible = showPlayIcon,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .zIndex(20f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(75.dp),
                    painter = painterResource(R.drawable.ic_play_solid),
                    contentDescription = null,
                    tint = Color.White.copy(0.5f)
                )
            }
        }
    }
}
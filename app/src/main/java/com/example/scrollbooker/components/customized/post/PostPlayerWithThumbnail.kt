package com.example.scrollbooker.components.customized.post

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.post.components.PostPlayerView

@Composable
fun PostPlayerWithThumbnail(
    player: ExoPlayer,
    showPlayIcon: Boolean = false,
    thumbnailUrl: String
) {
    Box(Modifier.fillMaxSize()) {
        PostPlayerView(
            player = player,
            thumbnailUrl = thumbnailUrl
        )

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
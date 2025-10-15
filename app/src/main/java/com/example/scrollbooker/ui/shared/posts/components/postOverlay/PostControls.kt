package com.example.scrollbooker.ui.shared.posts.components.postOverlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.shared.posts.components.VideoSlider
import kotlinx.coroutines.delay

@Composable
fun PostControls(
    player: ExoPlayer,
    post: Post,
    isPlaying: Boolean,
    visible: Boolean
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .zIndex(5f),
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

    var progress by remember(post.id) { mutableFloatStateOf(0f) }

    LaunchedEffect(player) {
        while (true) {
            val duration = player.duration.takeIf { it > 0 } ?: 1L
            val position = player.currentPosition
            progress = (position / duration.toFloat()).coerceIn(0f, 1f)
            delay(100)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        VideoSlider(
            progress = progress,
            isPlaying = isPlaying,
            onSeek = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}
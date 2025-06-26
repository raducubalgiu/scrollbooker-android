package com.example.scrollbooker.modules.posts.common

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.scrollbooker.core.util.VideoPlayerCache
import timber.log.Timber

@OptIn(UnstableApi::class)
@Composable
fun rememberCachedExoPlayer(context: Context, url: String?): ExoPlayer {
    val exoPlayer = remember(url) {
        Timber.tag("Exo Player").e("Exo Player created for $url")
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.cacheDataSourceFactory))
            .build()
            .apply {
                setMediaItem(MediaItem.fromUri(url ?: ""))
                repeatMode = Player.REPEAT_MODE_ALL
                volume = 1f
                prepare()
            }
    }
    return exoPlayer
}
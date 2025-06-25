package com.example.scrollbooker.components.customized.post.common

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.scrollbooker.core.util.VideoPlayerCache
import timber.log.Timber

object VideoPlayerPool {
    private val playerMap = mutableMapOf<String, ExoPlayer>()

    @OptIn(UnstableApi::class)
    fun get(context: Context, url: String): ExoPlayer {
        return playerMap.getOrPut(url) {

            ExoPlayer.Builder(context)
                .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(context)))
                .build().apply {
                    setMediaItem(MediaItem.fromUri(url))
                    repeatMode = Player.REPEAT_MODE_ALL
                    volume = 1f
                    prepare()
                }.also {
                    Timber.tag("Exo Player View Model").e("Create view model for url: $url")
                }
        }
    }

    fun releaseAll() {
        playerMap.values.forEach { it.release() }
        playerMap.clear()
    }
}
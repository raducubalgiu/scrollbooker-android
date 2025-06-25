package com.example.scrollbooker.components.customized.post.common

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.scrollbooker.core.util.VideoPlayerCache
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    private val playerMap = mutableMapOf<String, ExoPlayer>()

    private val _activePlayers = MutableStateFlow<List<String>>(emptyList())
    val activePlayers: StateFlow<List<String>> = _activePlayers

    @OptIn(UnstableApi::class)
    fun getOrCreatePlayer(url: String): ExoPlayer {
        return playerMap.getOrPut(url) {
            Timber.tag("Exo Player View Model").e("Create view model for url: $url")

            ExoPlayer.Builder(context)
                .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(context)))
                .build().apply {
                    setMediaItem(MediaItem.fromUri(url))
                    playWhenReady = false
                    repeatMode = Player.REPEAT_MODE_ALL
                    volume = 1f
                    prepare()
                }
        }
    }

    fun play(url: String?) {
        if(url != null) {
            _activePlayers.update { listOf(url) }
            playerMap.forEach { (key, player) ->
                player.playWhenReady = key == url
            }
        }
    }

    fun stopAll() {
        playerMap.values.forEach {
            it.playWhenReady = false
        }
        _activePlayers.value = emptyList()
    }

    fun releaseAll() {
        playerMap.values.forEach { it.release() }
        playerMap.clear()
    }

    override fun onCleared() {
        super.onCleared()
        releaseAll()
    }
}
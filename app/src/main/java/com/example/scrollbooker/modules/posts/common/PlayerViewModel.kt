package com.example.scrollbooker.modules.posts.common
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.scrollbooker.core.util.VideoPlayerCache
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _exoPlayer: ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(context)))
        .build().apply {
            repeatMode = Player.REPEAT_MODE_ONE
            volume = 1f
        }

    val exoPlayer: ExoPlayer get() = _exoPlayer

    private var currentUrl: String? = null

    fun playVideo(url: String) {
        if(currentUrl == url) return
        currentUrl = url
        _exoPlayer.setMediaItem(MediaItem.fromUri(url))
        _exoPlayer.prepare()
        _exoPlayer.playWhenReady = true
    }

    fun pauseVideo() {
        _exoPlayer.playWhenReady = false
    }

    fun releasePlayer() {
        _exoPlayer.release()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}
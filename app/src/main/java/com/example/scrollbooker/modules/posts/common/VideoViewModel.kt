package com.example.scrollbooker.modules.posts.common
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    private val exoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(DefaultMediaSourceFactory(context))
        .build()

    private val _currentMediaItem = MutableStateFlow<MediaItem?>(null)
    val currentMediaItem: StateFlow<MediaItem?> = _currentMediaItem

    fun play(mediaItem: MediaItem) {
        if(exoPlayer.currentMediaItem?.mediaId != mediaItem.mediaId) {
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
        exoPlayer.playWhenReady = true
        _currentMediaItem.value = mediaItem
    }

    fun pause() {
        exoPlayer.playWhenReady = false
    }

    fun getPlayer(): ExoPlayer = exoPlayer

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}
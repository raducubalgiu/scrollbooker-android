package com.example.scrollbooker.ui.camera

import android.content.Context
import android.graphics.Bitmap
import android.os.HandlerThread
import android.os.Process
import androidx.lifecycle.ViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.scrollbooker.components.customized.MediaLibraryBottomSheet.MediaFile
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _isSheetOpen = MutableStateFlow<Boolean>(false)
    val isSheetOpen: StateFlow<Boolean> = _isSheetOpen.asStateFlow()

    private val _selectedVideo = MutableStateFlow<MediaItem?>(null)
    val selectedVideo: StateFlow<MediaItem?> = _selectedVideo.asStateFlow()

    private val _isPrepared = MutableStateFlow<Boolean>(false)
    val isPrepared: StateFlow<Boolean> = _isPrepared.asStateFlow()

    private val _isPlaying = MutableStateFlow<Boolean>(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var _player: ExoPlayer? = null
    val player: ExoPlayer get() = ensurePlayer()

    private val _isCameraMounted = MutableStateFlow<Boolean>(false)
    val isCameraMounted: StateFlow<Boolean> = _isCameraMounted.asStateFlow()

    fun setIsCameraMounted(mounted: Boolean) {
        _isCameraMounted.value = mounted
    }

    fun openSheet(open: Boolean) {
        _isSheetOpen.value = open
    }

    private fun ensurePlayer(): ExoPlayer {
        if(_player != null) return _player!!

        _player = ExoPlayer.Builder(context)
            .setHandleAudioBecomingNoisy(true)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(), false
            )
            .build().also {
                it.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                it.playWhenReady = false
                it.addListener(playerListener)
            }

        return _player!!
    }

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            val ready = state == Player.STATE_READY
            _isPrepared.value = ready
            if(ready) _player?.play()
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
        }
    }

    fun setVideo(video: MediaItem) {
        _selectedVideo.value = video
    }

    fun prepareSelectedVideo() {
        val item = _selectedVideo.value ?: return
        _isPrepared.value = false
        val p = ensurePlayer()

        p.stop()
        p.clearMediaItems()

        p.setMediaItem(item)
        p.prepare()
    }

    fun play() = _player?.play()
    fun pause() = _player?.pause()

    fun releasePlayer() {
        _player?.removeListener(playerListener)
        _player?.release()
        _player = null
        _isPrepared.value = false
    }

    override fun onCleared() {
        releasePlayer()
        super.onCleared()
    }
}
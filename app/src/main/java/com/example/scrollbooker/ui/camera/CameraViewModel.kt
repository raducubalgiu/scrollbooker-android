package com.example.scrollbooker.ui.camera

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {
    val cameraController: LifecycleCameraController by lazy {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(LifecycleCameraController.VIDEO_CAPTURE)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    private val _isBound = MutableStateFlow(false)
    val isBound: StateFlow<Boolean> = _isBound.asStateFlow()

    fun bindIfNeeded(owner: LifecycleOwner) {
        if(_isBound.value) return
        cameraController.bindToLifecycle(owner)
        _isBound.value = true
    }

    fun switchCamera() {
        cameraController.cameraSelector =
            if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
    }

    private val _selectedVideo = MutableStateFlow<MediaItem?>(null)
    val selectedVideo: StateFlow<MediaItem?> = _selectedVideo.asStateFlow()

    private val _isPrepared = MutableStateFlow<Boolean>(false)
    val isPrepared: StateFlow<Boolean> = _isPrepared.asStateFlow()

    private val _isPreparingSelectedVideo = MutableStateFlow<Uri?>(null)
    val isPreparingSelectedVideo: StateFlow<Uri?> = _isPreparingSelectedVideo.asStateFlow()

    private var _player: ExoPlayer? = null
    val player: ExoPlayer get() = ensurePlayer()

    private val _isCameraMounted = MutableStateFlow<Boolean>(false)
    val isCameraMounted: StateFlow<Boolean> = _isCameraMounted.asStateFlow()

    fun setIsCameraMounted(mounted: Boolean) {
        _isCameraMounted.value = mounted
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

            if(ready) {
                _isPreparingSelectedVideo.value = null
                _player?.play()
            }
        }
    }

    fun setVideo(video: MediaItem) {
        _selectedVideo.value = video
    }

    private var prepareJob: Job? = null

    fun prepareSelectedVideo() {
        val item = _selectedVideo.value ?: return
        _isPrepared.value = false
        _isPreparingSelectedVideo.value = item.localConfiguration?.uri

        prepareJob?.cancel()
        prepareJob = viewModelScope.launch(Dispatchers.Main) {
            val player = ensurePlayer()
            player.setMediaItem(item)
            player.prepare()
        }
    }

    fun releasePlayer() {
        _player?.pause()
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
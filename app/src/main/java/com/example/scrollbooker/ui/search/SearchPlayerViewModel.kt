package com.example.scrollbooker.ui.search

import android.content.Context
import android.os.HandlerThread
import android.os.Process
import androidx.lifecycle.ViewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.scrollbooker.core.util.VideoPlayerCache
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchPlayerUIState(
    val currentId: Int? = null,
    val isBuffering: Boolean = false,
    val isReady: Boolean = false,
    val isPlaying: Boolean = false,
    val isFirstFrameRendered: Boolean = false
)

@UnstableApi
@HiltViewModel
class SearchPlayerViewModel @Inject constructor(
    @ApplicationContext private val application: Context
): ViewModel() {
    private val _playerState = MutableStateFlow(SearchPlayerUIState())
    val playerState: MutableStateFlow<SearchPlayerUIState> = _playerState

    private var playerThread: HandlerThread = HandlerThread("ExoPlayer Thread", Process.THREAD_PRIORITY_AUDIO)
        .apply { start() }

    private fun createLoadControl(): DefaultLoadControl {
        return DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                1500,
                5000,
                500,
                1500
            )
            .setTargetBufferBytes(C.LENGTH_UNSET)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()
    }

    val player: ExoPlayer = ExoPlayer.Builder(application.applicationContext)
        .setLoadControl(createLoadControl())
        .setPlaybackLooper(playerThread.looper)
        .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(application.applicationContext)))
        .build().apply {
            repeatMode = Player.REPEAT_MODE_OFF
            playWhenReady = false

            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _playerState.update {
                        it.copy(
                            isPlaying = isPlaying
                        )
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    _playerState.update {
                        it.copy(
                            isBuffering = state == Player.STATE_BUFFERING,
                            isReady = state == Player.STATE_READY
                        )
                    }
                }

                override fun onRenderedFirstFrame() {
                    _playerState.update {
                        it.copy(
                            isFirstFrameRendered = true
                        )
                    }
                }
            })
        }

    private fun clearPlayerState(currentId: Int?) {
        _playerState.update {
            it.copy(
                currentId = currentId,
                isReady = false,
                isBuffering = false,
                isPlaying = false,
                isFirstFrameRendered = false
            )
        }
    }

    fun play(id: Int, url: String) {
        //if(_playerState.value.currentId == id && player.playWhenReady) return
        clearPlayerState(currentId = id)

        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
        clearPlayerState(currentId = null)
    }

    fun releasePlayer(id: Int?) {
        id?.let {
            player.playWhenReady = false
            clearPlayerState(currentId = null)
        }
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}
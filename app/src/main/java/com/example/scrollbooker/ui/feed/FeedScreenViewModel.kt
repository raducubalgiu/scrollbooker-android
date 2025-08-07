package com.example.scrollbooker.ui.feed

import android.content.Context
import android.os.HandlerThread
import android.os.Process
import androidx.lifecycle.ViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.scrollbooker.core.util.VideoPlayerCache
import com.example.scrollbooker.entity.social.post.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

data class PlayerUIState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isFirstFrameRendered: Boolean = false,
    val hasStartedPlayback: Boolean = false
)

@UnstableApi
@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    @ApplicationContext private val application: Context
) : ViewModel() {
    private val playerPool = mutableMapOf<Int, ExoPlayer>()
    private val _playerStates = mutableMapOf<Int, MutableStateFlow<PlayerUIState>>()

    private var playerThread: HandlerThread = HandlerThread("ExoPlayer Thread", Process.THREAD_PRIORITY_AUDIO)
        .apply { start() }
    var playbackStartTimeMs = C.TIME_UNSET

    private val firstFrameListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            val timeToFirstFrameMs = System.currentTimeMillis() - playbackStartTimeMs
            Timber.tag("PreloadManager").d("\t\tTime to first Frame = $timeToFirstFrameMs ")
            super.onRenderedFirstFrame()
        }
    }

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

    fun getPlayerState(postId: Int): StateFlow<PlayerUIState> {
        return _playerStates.getOrPut(postId) { MutableStateFlow(PlayerUIState()) }
    }

    private fun updatePlayerState(
        postId: Int,
        transform: (PlayerUIState) -> PlayerUIState
    ) {
        _playerStates[postId]?.let { mutableFlow ->
            mutableFlow.value = transform(mutableFlow.value)
        }
    }

    private val playerListeners = mutableMapOf<Int, Player.Listener>()

    private fun attachPlayerStateListener(postId: Int, player: ExoPlayer) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updatePlayerState(postId) { it.copy(
                    isPlaying = isPlaying,
                    hasStartedPlayback = isPlaying || it.hasStartedPlayback)
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                updatePlayerState(postId) { it.copy(
                    isBuffering = state == Player.STATE_BUFFERING
                )}
            }

            override fun onRenderedFirstFrame() {
                updatePlayerState(postId) { it.copy(
                    isFirstFrameRendered = true
                )}
            }
        }

        player.addListener(listener)
        playerListeners[postId] = listener
    }

    private fun resetInactivePlayerStates(postId: Int) {
        _playerStates
            .filterKeys { it != postId }
            .forEach { (_, state) ->
                state.value = PlayerUIState()
            }
    }

    private fun preloadVideo(post: Post?) {
        if(post == null) return

        val player = getOrCreatePlayer(post)
        val mediaItem = MediaItem.fromUri(post.mediaFiles.first().url)

        if(player.currentMediaItem?.localConfiguration?.uri == mediaItem.localConfiguration?.uri) return

        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = false
    }

    fun initializePlayer(
        post: Post?,
        previousPost: Post?,
        nextPost: Post?
    ) {
        if (post == null) return
        val player = getOrCreatePlayer(post = post)

        pauseUnusedPlayers(post.id)
        resetInactivePlayerStates(post.id)

        val newMediaItem = MediaItem.fromUri(post.mediaFiles.first().url)

        if(player.currentMediaItem?.localConfiguration?.uri != newMediaItem.localConfiguration?.uri) {
            player.setMediaItem(newMediaItem)
            player.prepare()
        }
        player.playWhenReady = true

        preloadVideo(previousPost)
        preloadVideo(nextPost)

        playbackStartTimeMs = System.currentTimeMillis()
        Timber.tag("PreloadManager").d("Video Playing $post ")
    }

    fun getOrCreatePlayer(post: Post): ExoPlayer {
        return playerPool.getOrPut(post.id) {
            ExoPlayer.Builder(application.applicationContext)
                .setLoadControl(createLoadControl())
                .setPlaybackLooper(playerThread.looper)
                .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(application.applicationContext)))
                .setHandleAudioBecomingNoisy(true)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(C.USAGE_MEDIA)
                        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                        .build(), true
                )
                .build()
                .also {
                    it.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    it.playWhenReady = false
                    it.addListener(firstFrameListener)
                    attachPlayerStateListener(post.id, it)
                }
        }
    }

    fun togglePlayer(postId: Int) {
        val player = playerPool[postId] ?: return

        if(player.isPlaying) {
            player.pause()
            player.playWhenReady = false
        } else {
            player.playWhenReady = true
        }
    }

    fun pauseIfPlaying(postId: Int) {
        val player = playerPool[postId]
        if(player?.isPlaying == true) {
            player.pause()
            player.playWhenReady = false
        }
    }

    fun resumeIfPlaying(postId: Int) {
        val player = playerPool[postId]
        if(player != null && !player.isPlaying) {
            player.playWhenReady = true
        }
    }

    fun pauseUnusedPlayers(visiblePostId: Int) {
        playerPool.forEach { (postId, player) ->
            if(postId != visiblePostId) {
                player.playWhenReady = false
                player.pause()
            }
        }
    }

    fun releaseInactivePlayers(postId: Int) {
        val currentPlayer = playerPool[postId]

        playerPool.entries
            .filter { it.key != postId }
            .forEach {
                it.value.release()
            }

        playerPool.clear()

        if(currentPlayer != null) {
            playerPool[postId] = currentPlayer
        }
    }

    private fun limitPlayerPoolSize(postId: Int) {
        if(playerPool.size > 5) {
            playerPool.entries
                .filter { it.key != postId }
                .take(playerPool.size - 5)
                .forEach {
                    Timber.d("Release player for postId: $postId")
                    it.value.release()
                    playerPool.remove(it.key)
                }
        }
    }

    fun releasePlayer(postId: Int?) {
        postId?.let { id ->
            playerPool[id]?.apply {
                playWhenReady = false
                pause()
                playerListeners.remove(id)?.let { removeListener(it) }
                removeListener(firstFrameListener)
            }
            _playerStates.remove(id)

            //resetInactivePlayerStates(postId)
            limitPlayerPoolSize(postId)
        }
    }

//    override fun onCleared() {
//        super.onCleared()
//
//        playerPool.values.forEach {
//            it.release()
//        }
//        playerPool.clear()
//        playerThread.quitSafely()
//    }
}
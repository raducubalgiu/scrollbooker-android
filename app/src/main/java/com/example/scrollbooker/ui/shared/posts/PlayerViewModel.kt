package com.example.scrollbooker.ui.shared.posts

import android.content.Context
import android.os.HandlerThread
import android.os.Process
import androidx.annotation.OptIn
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
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

data class PlayerUIState(
    val isReady: Boolean = false,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val isFirstFrameRendered: Boolean = false,
    val hasStartedPlayback: Boolean = false
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
): ViewModel() {
    private val playerPool = mutableMapOf<Int, ExoPlayer>()
    private val _playerStates = mutableMapOf<Int, MutableStateFlow<PlayerUIState>>()

    private var playerThread: HandlerThread = HandlerThread("ExoPlayer Thread", Process.THREAD_PRIORITY_AUDIO)
        .apply { start() }

    private val _currentPost = MutableStateFlow<Post?>(null)
    val currentPost: StateFlow<Post?> = _currentPost.asStateFlow()

    @OptIn(UnstableApi::class)
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
                    isReady = state == Player.STATE_READY,
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

    fun getPlayerState(postId: Int): StateFlow<PlayerUIState> {
        return _playerStates.getOrPut(postId) { MutableStateFlow(PlayerUIState()) }
    }

    private fun updatePlayerState(postId: Int, transform: (PlayerUIState) -> PlayerUIState) {
        _playerStates[postId]?.let { mutableFlow ->
            mutableFlow.value = transform(mutableFlow.value)
        }
    }

    fun initializePlayer(
        post: Post?,
        previousPost: Post?,
        nextPost: Post?
    ) {
        if (post == null) return
        _currentPost.value = post

        val player = getOrCreatePlayer(post)

        val newMediaItem = MediaItem.fromUri(post.mediaFiles.first().url)
        val sameItem = player.currentMediaItem?.localConfiguration?.uri == newMediaItem.localConfiguration?.uri

        if(!sameItem) {
            player.setMediaItem(newMediaItem)
            player.prepare()
            player.seekTo(0)
        }
        player.playWhenReady = true

        resetInactivePlayerStates(post.id)
        preloadVideo(previousPost)
        preloadVideo(nextPost)
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

    @OptIn(UnstableApi::class)
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
            }

            resetInactivePlayerStates(postId)
            limitPlayerPoolSize(postId)
        }
    }

    override fun onCleared() {
        super.onCleared()

        playerPool.values.forEach {
            it.release()
        }
        playerPool.clear()
        playerThread.quitSafely()
    }
}
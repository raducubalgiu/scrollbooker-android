package com.example.scrollbooker.ui.feed

import android.content.Context
import android.net.Uri
import android.os.HandlerThread
import android.os.Process
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.VideoPlayerCache
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val getFollowingPostsUseCase: GetFollowingPostsUseCase
) : ViewModel() {
    private val _followingPosts: Flow<PagingData<Post>> by lazy {
        getFollowingPostsUseCase()
            .cachedIn(viewModelScope)
    }
    val followingPosts: Flow<PagingData<Post>> get() = _followingPosts

    // Single player instance - in the future, we can implement a pool of players to improve
    // latency and allow for concurrent playback
    var player by mutableStateOf<ExoPlayer?>(null)

    // Playback thread; Internal playback / preload operations are running on the playback thread.
    private val playerThread: HandlerThread =
        HandlerThread("playback-thread", Process.THREAD_PRIORITY_AUDIO)

    var playbackStartTimeMs = C.TIME_UNSET

    private val firstFrameListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            val timeToFirstFrameMs = System.currentTimeMillis() - playbackStartTimeMs
            Timber.tag("PreloadManager").d("\t\tTime to first Frame = $timeToFirstFrameMs ")
            super.onRenderedFirstFrame()
        }
    }

    @OptIn(UnstableApi::class) // https://developer.android.com/guide/topics/media/media3/getting-started/migration-guide#unstableapi
    fun initializePlayer() {
        if (player != null) return

        // Reduced buffer durations since the primary use-case is for short-form videos
        val loadControl =
            DefaultLoadControl.Builder().setBufferDurationsMs(5_000, 20_000, 5_00, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
                .setPrioritizeTimeOverSizeThresholds(true).build()

        playerThread.start()

        val newPlayer = ExoPlayer
            .Builder(application.applicationContext)
            .setLoadControl(loadControl)
            .setPlaybackLooper(playerThread.looper)
            .build()
            .also {
                it.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                it.playWhenReady = true
                it.addListener(firstFrameListener)
            }

        player = newPlayer
    }

    fun releasePlayer() {
        player?.apply {
            removeListener(firstFrameListener)
            release()
        }
        playerThread.quit()
        player = null
    }

    fun changePlayerItem(uri: Uri?, currentPlayingIndex: Int) {
        if (player == null) return

        player?.apply {
            stop()
            if (uri != null) {
                // Set the right source to play
                val mediaItem = MediaItem.fromUri(uri)

//                if (enablePreloadManager) {
//                    val mediaSource = preloadManager.getMediaSource(mediaItem)
//                    Log.d("PreloadManager", "Mediasource $mediaSource ")
//
//                    if (mediaSource == null) {
//                        setMediaItem(mediaItem)
//                    } else {
//                        // Use the preloaded media source
//                        setMediaSource(mediaSource)
//                    }
//                    preloadManager.setCurrentPlayingIndex(currentPlayingIndex)
//                } else {
//                    setMediaItem(mediaItem)
//                }

                setMediaItem(mediaItem)

                playbackStartTimeMs = System.currentTimeMillis()
                Timber.tag("PreloadManager").d("Video Playing $uri ")
                prepare()
            } else {
                clearMediaItems()
            }
        }
    }
}
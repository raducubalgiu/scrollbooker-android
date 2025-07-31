package com.example.scrollbooker.ui.feed

import android.content.Context
import android.net.Uri
import android.os.HandlerThread
import android.os.Process
import androidx.annotation.OptIn
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

@UnstableApi
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

    private val playerPool = mutableMapOf<Int, ExoPlayer>()
    private val currentMediaItemMap = mutableMapOf<Int, MediaItem>()

    private var playerThread: HandlerThread = HandlerThread("ExoPlayer Thread", Process.THREAD_PRIORITY_AUDIO).apply { start() }

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
            .setBufferDurationsMs(5_000, 20_000, 5_00, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS)
                .setPrioritizeTimeOverSizeThresholds(true).build()
    }

    fun changePlayerItem(post: Post?) {
        if (post == null) return
        val player = getOrCreatePlayer(post = post)
//        val newMediaItem = MediaItem.fromUri(post.mediaFiles.first().url)
//
//        val currentItem = currentMediaItemMap[post.id]
//
//        if(currentItem != newMediaItem) {
//            player.setMediaItem(newMediaItem)
//            currentMediaItemMap[post.id] = newMediaItem
//            player.prepare()
//        }

        val mediaItem = MediaItem.fromUri(post.mediaFiles.first().url)
        player.setMediaItem(mediaItem)

        player.prepare()
        player.playWhenReady = true

        playbackStartTimeMs = System.currentTimeMillis()
        Timber.tag("PreloadManager").d("Video Playing $post ")
    }

    fun getOrCreatePlayer(post: Post): ExoPlayer {
        return playerPool.getOrPut(post.id) {
            ExoPlayer.Builder(application.applicationContext)
                .setLoadControl(createLoadControl())
                .setPlaybackLooper(playerThread.looper)
                .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(application.applicationContext)))
                .build()
                .also {
                    it.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    it.playWhenReady = false
                    it.addListener(firstFrameListener)
                }
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

    fun releasePlayer() {
        playerPool.forEach { (_, player) ->
            player.removeListener(firstFrameListener)
            player.release()
        }

        playerPool.clear()
        playerThread.quitSafely()
    }

    override fun onCleared() {
        super.onCleared()

        releasePlayer()
        playerThread.quitSafely()
    }
}
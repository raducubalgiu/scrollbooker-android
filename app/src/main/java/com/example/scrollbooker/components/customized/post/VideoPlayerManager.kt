package com.example.scrollbooker.components.customized.post

import android.app.Application
import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.mutableStateMapOf
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.scrollbooker.entity.social.post.domain.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoPlayerManager @Inject constructor(
    private val application: Application
) {
    private val maxPlayers = 3
    private val pool = ArrayDeque<ExoPlayer>(maxPlayers)

    private val indexToPlayer: SnapshotStateMap<String, ExoPlayer> = mutableStateMapOf()
    private val indexToPostId: SnapshotStateMap<String, Int> = mutableStateMapOf()

    private var activeTabKey: String? = null

    private var focusedIndex: Int? = null
    private val windowMutex = Mutex()

    private val _userPausedPostIds = MutableStateFlow<Set<Int>>(emptySet())
    val userPausedPostIds: StateFlow<Set<Int>> = _userPausedPostIds.asStateFlow()

    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        repeat(maxPlayers) { pool.add(createPlayer(application)) }
    }

    @OptIn(UnstableApi::class)
    private fun createLoadControl(): DefaultLoadControl {
        return DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                3000,
                10000,
                1000,
                2000
            )
            .setTargetBufferBytes(C.LENGTH_UNSET)
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()
    }

    @OptIn(UnstableApi::class)
    private fun createPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setLoadControl(createLoadControl())
            .setHandleAudioBecomingNoisy(true)
            .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory()))
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(), true
            )
            .build()
            .apply {
                repeatMode = Player.REPEAT_MODE_ONE
                playWhenReady = false
            }
    }

    private fun buildMapKey(tabKey: String, index: Int): String = "$tabKey#$index"

    fun ensureWindow(
        tabKey: String,
        centerIndex: Int,
        getPost: (Int) -> Post?
    ) {
        managerScope.launch {
            windowMutex.withLock {
                ensureWindowInternal(tabKey, centerIndex, getPost)
            }
        }
    }

    private fun ensureWindowInternal(
        tabKey: String,
        centerIndex: Int,
        getPost: (Int) -> Post?
    ) {
        val desiredIndices = listOf(centerIndex - 1, centerIndex, centerIndex + 1)
            .filter { it >= 0 }

        val desiredKeys = desiredIndices.map { buildMapKey(tabKey, it) }

        val toRemove = indexToPlayer.keys.filter { it.startsWith("$tabKey#") && it !in desiredKeys }
        toRemove.forEach { key ->
            indexToPlayer.remove(key)?.let { player ->
                indexToPostId.remove(key)
                resetPlayer(player)
                pool.addLast(player)
            }
        }

        desiredIndices.forEach { idx ->
            if (idx < 0) return@forEach
            val post = getPost(idx) ?: return@forEach

            val currentKey = buildMapKey(tabKey, idx)
            val existing = indexToPlayer[currentKey]
            val existingPostId = indexToPostId[currentKey]
            if (existing != null && existingPostId == post.id) return@forEach

            val player = existing ?: pool.removeFirstOrNull() ?: return@forEach

            if (existing != null) {
                resetPlayer(player)
            }

            indexToPlayer[currentKey] = player
            indexToPostId[currentKey] = post.id

            prepareForPost(player, post)

            val isFocused = (idx == focusedIndex && activeTabKey == tabKey)
            val isUserPaused = _userPausedPostIds.value.contains(post.id)
            val isTabActiveGlobal = (activeTabKey == tabKey)

            player.playWhenReady = isFocused && !isUserPaused && isTabActiveGlobal

            if (!isFocused || isUserPaused || !isTabActiveGlobal) player.pause()
        }

        applyFocus(centerIndex)
    }

    fun onPageSettled(tabKey: String, index: Int) {
        if (activeTabKey != tabKey) return
        focusedIndex = index
        applyFocus(index)
    }

    private fun applyFocus(index: Int) {
        indexToPlayer.forEach { (key, player) ->
            val parts = key.split("#")
            val tabKey = parts.getOrNull(0) ?: ""
            val idx = parts.getOrNull(1)?.toIntOrNull() ?: -1

            val postId = indexToPostId[key]
            val isUserPaused = postId != null && _userPausedPostIds.value.contains(postId)
            val isTabActiveGlobal = (activeTabKey == tabKey)

            val shouldPlay = (idx == index) && !isUserPaused && isTabActiveGlobal

            player.playWhenReady = shouldPlay

            if (!shouldPlay) {
                player.pause()
            }
        }
    }

    private fun prepareForPost(player: ExoPlayer, post: Post) {
        val mediaItem = MediaItem.fromUri(post.mediaFiles.first().url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.seekTo(0)
    }

    private fun resetPlayer(player: ExoPlayer) {
        player.playWhenReady = false
        player.seekTo(0)
    }

    fun getPlayerForIndex(tabKey: String, index: Int): ExoPlayer? {
        return indexToPlayer[buildMapKey(tabKey, index)]
    }

    fun togglePlayer(tabKey: String, index: Int) {
        val currentKey = buildMapKey(tabKey, index)
        val player = getPlayerForIndex(tabKey, index) ?: return
        val postId = indexToPostId[currentKey] ?: return

        val isFocused = (index == focusedIndex && activeTabKey == tabKey)

        if (player.isPlaying) {
            player.playWhenReady = false
            _userPausedPostIds.update { it + postId }
        } else {
            _userPausedPostIds.update { it - postId }

            if (isFocused) {
                player.playWhenReady = true
            } else {
                focusedIndex = index
                applyFocus(index)
            }
        }
    }

    fun resumePlayerOnTabEnter(tabKey: String, currentIndex: Int) {
        activeTabKey = tabKey
        focusedIndex = currentIndex

        val currentKey = buildMapKey(tabKey, currentIndex)
        val player = getPlayerForIndex(tabKey, currentIndex) ?: return
        val postId = indexToPostId[currentKey] ?: return
        val isUserPaused = _userPausedPostIds.value.contains(postId)

        applyFocus(currentIndex)

        if (!isUserPaused) {
            player.playWhenReady = true
        }
    }

    fun stopDetailSession(tabKey: String) {
        if (activeTabKey == tabKey) {
            activeTabKey = null
        }
        indexToPlayer.forEach { (key, player) ->
            if (key.startsWith("$tabKey#")) {
                player.playWhenReady = false
            }
        }
    }

    fun releaseAllPlayers() {
        indexToPlayer.clear()
        indexToPostId.clear()

        pool.forEach { it.release() }
        pool.clear()
    }
}

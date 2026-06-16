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
    private val maxPlayers = 5
    private val pool = ArrayDeque<ExoPlayer>(maxPlayers)

    private val indexToPlayer: SnapshotStateMap<String, ExoPlayer> = mutableStateMapOf()
    private val indexToPostId: SnapshotStateMap<String, Int> = mutableStateMapOf()
    private val indexToLastPosition = mutableMapOf<String, Long>()

    private val windowMutex = Mutex()
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _userPausedPostIds = MutableStateFlow<Set<Int>>(emptySet())
    val userPausedPostIds: StateFlow<Set<Int>> = _userPausedPostIds.asStateFlow()

    private var activeScopeKey: String? = null

    init {
        repeat(maxPlayers) { pool.add(createPlayer(application)) }
    }

    @OptIn(UnstableApi::class)
    private fun createLoadControl(): DefaultLoadControl {
        return DefaultLoadControl.Builder()
            .setBufferDurationsMs(3000, 10000, 1000, 2000)
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

    private fun buildMapKey(scopeKey: String, index: Int): String = "$scopeKey#$index"

    fun getPlayerForIndex(scopeKey: String, index: Int): ExoPlayer? {
        return indexToPlayer[buildMapKey(scopeKey, index)]
    }

    fun activateScreenScope(scopeKey: String) {
        activeScopeKey = scopeKey
        indexToPlayer.forEach { (key, player) ->
            if (!key.startsWith("$scopeKey#")) {
                player.playWhenReady = false
                player.pause() // Înghețare pură în RAM
            }
        }
    }

    fun freezeScreenScope(scopeKey: String) {
        indexToPlayer.forEach { (key, player) ->
            if (key.startsWith("$scopeKey#")) {
                player.playWhenReady = false
                player.pause()
            }
        }
    }

    private fun rentPlayerFromPool(currentScopeKey: String): ExoPlayer? {
        val free = pool.removeFirstOrNull()
        if (free != null) return free

        indexToPlayer.keys.toList().forEach { key ->
            if (!key.startsWith("$currentScopeKey#")) {
                val player = indexToPlayer.remove(key)
                if (player != null) {
                    indexToPostId.remove(key)
                    indexToLastPosition.remove(key)

                    player.playWhenReady = false
                    player.stop()
                    player.clearMediaItems()
                    return player
                }
            }
        }
        return null
    }

    fun ensureWindow(
        scopeKey: String,
        centerIndex: Int,
        isScreenActive: Boolean,
        getPost: (Int) -> Post?
    ) {
        if (!isScreenActive) {
            freezeScreenScope(scopeKey)
            return
        }

        if (activeScopeKey != null && activeScopeKey != scopeKey) return

        managerScope.launch {
            windowMutex.withLock {
                val desiredIndices = listOf(centerIndex - 1, centerIndex, centerIndex + 1).filter { it >= 0 }
                val desiredKeys = desiredIndices.map { buildMapKey(scopeKey, it) }

                val toRemove = indexToPlayer.keys.filter { it.startsWith("$scopeKey#") && it !in desiredKeys }
                toRemove.forEach { key ->
                    indexToPlayer.remove(key)?.let { player ->
                        indexToPostId.remove(key)
                        indexToLastPosition.remove(key)
                        resetPlayerFull(player)
                        if (!pool.contains(player)) pool.addLast(player)
                    }
                }

                desiredIndices.forEach { idx ->
                    val post = getPost(idx) ?: return@forEach
                    val currentKey = buildMapKey(scopeKey, idx)

                    val existing = indexToPlayer[currentKey]
                    val existingPostId = indexToPostId[currentKey]

                    if (existing != null && existingPostId == post.id) {
                        val isFocused = (idx == centerIndex)
                        val isUserPaused = _userPausedPostIds.value.contains(post.id)

                        existing.playWhenReady = isFocused
                        if (isFocused && !isUserPaused) {
                            if (!existing.isPlaying) existing.play()
                        } else {
                            existing.pause()
                        }
                        return@forEach
                    }

                    val player = existing ?: rentPlayerFromPool(scopeKey) ?: return@forEach

                    if (existing == null) {
                        player.playWhenReady = false
                        player.stop()
                        player.clearMediaItems()
                        player.seekTo(0)
                    } else {
                        resetPlayerFull(player)
                    }

                    indexToPlayer[currentKey] = player
                    indexToPostId[currentKey] = post.id

                    val mediaItem = MediaItem.fromUri(post.mediaFiles.first().url)
                    player.setMediaItem(mediaItem)
                    player.prepare()

                    val isFocused = (idx == centerIndex)
                    val isUserPaused = _userPausedPostIds.value.contains(post.id)
                    player.playWhenReady = isFocused && !isUserPaused
                    if (!player.playWhenReady) player.pause()
                }
            }
        }
    }

    fun onPageSettled(scopeKey: String, centerIndex: Int, isScreenActive: Boolean) {
        if (!isScreenActive || activeScopeKey != scopeKey) return

        indexToPlayer.forEach { (key, player) ->
            if (key.startsWith("$scopeKey#")) {
                val idx = key.split("#").getOrNull(1)?.toIntOrNull() ?: -1
                val postId = indexToPostId[key]
                val isUserPaused = postId != null && _userPausedPostIds.value.contains(postId)

                val shouldPlay = (idx == centerIndex) && !isUserPaused
                player.playWhenReady = shouldPlay
                if (shouldPlay) {
                    if (!player.isPlaying) player.play()
                } else {
                    player.pause()
                }
            }
        }
    }

    fun releaseScreenScope(scopeKey: String) {
        val keysToRemove = indexToPlayer.keys.filter { it.startsWith("$scopeKey#") }.toList()
        keysToRemove.forEach { key ->
            indexToPlayer.remove(key)?.let { player ->
                indexToPostId.remove(key)
                indexToLastPosition.remove(key)

                player.playWhenReady = false
                player.pause()

                if (!pool.contains(player)) {
                    pool.addLast(player)
                }
            }
        }

        if (activeScopeKey == scopeKey) {
            activeScopeKey = null
        }
    }

    private fun resetPlayerFull(player: ExoPlayer) {
        player.playWhenReady = false
        player.stop()
        player.clearVideoSurface()
        player.clearMediaItems()
        player.seekTo(0)
    }

    fun togglePlayer(scopeKey: String, index: Int) {
        val currentKey = buildMapKey(scopeKey, index)
        val player = indexToPlayer[currentKey] ?: return
        val postId = indexToPostId[currentKey] ?: return

        if (player.isPlaying) {
            player.playWhenReady = false
            player.pause()
            _userPausedPostIds.update { it + postId }
        } else {
            _userPausedPostIds.update { it - postId }
            player.playWhenReady = true
            player.play()
        }
    }
}



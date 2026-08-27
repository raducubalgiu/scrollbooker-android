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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.IdentityHashMap
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

    private val playerToKey = IdentityHashMap<ExoPlayer, String>()
    private val _playbackEvents = MutableSharedFlow<PlaybackEvent>(extraBufferCapacity = 64)
    val playbackEvents: SharedFlow<PlaybackEvent> = _playbackEvents.asSharedFlow()

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
            .setBufferDurationsMs(
                2500,
                15000,
                250,
                500
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

                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        val key = playerToKey[this@apply] ?: return
                        val postId = indexToPostId[key] ?: return

                        _playbackEvents.tryEmit(
                            PlaybackEvent(
                                scopeKey = key.substringBefore("#"),
                                postId = postId,
                                isPlaying = isPlaying,
                                positionMs = this@apply.currentPosition,
                                durationMs = this@apply.duration.takeIf { it > 0 }
                            )
                        )
                    }
                })
            }
    }

    private fun buildMapKey(scopeKey: String, index: Int): String = "$scopeKey#$index"

    fun getPlayerForIndex(scopeKey: String, index: Int): ExoPlayer? {
        return indexToPlayer[buildMapKey(scopeKey, index)]
    }

    /**
     * OPTIMIZAT: Mutăm buclele hardware pe un thread de background rapid
     * pentru a menține animația de tranziție de navigare la 120 FPS.
     */
    fun activateScreenScope(scopeKey: String) {
        activeScopeKey = scopeKey
        managerScope.launch(Dispatchers.Default) {
            for ((key, player) in indexToPlayer) {
                if (!key.startsWith("$scopeKey#")) {
                    withContext(Dispatchers.Main.immediate) {
                        player.playWhenReady = false
                        player.pause()
                    }
                }
            }
        }
    }

    /**
     * OPTIMIZAT: Control non-blocking pentru UI thread
     */
    fun freezeScreenScope(scopeKey: String) {
        managerScope.launch(Dispatchers.Default) {
            for ((key, player) in indexToPlayer) {
                if (key.startsWith("$scopeKey#")) {
                    withContext(Dispatchers.Main.immediate) {
                        player.playWhenReady = false
                        player.pause()
                    }
                }
            }
        }
    }

    /**
     * OPTIMIZAT: Căutare directă fără .toList() sau .filter() pentru a evita alocarea de obiecte inutile
     */
    private fun rentPlayerFromPool(currentScopeKey: String): ExoPlayer? {
        val free = pool.removeFirstOrNull()
        if (free != null) return free

        for (key in indexToPlayer.keys) {
            if (!key.startsWith("$currentScopeKey#")) {
                val player = indexToPlayer.remove(key)
                if (player != null) {
                    indexToPostId.remove(key)
                    playerToKey.remove(player) // ADD
                    player.playWhenReady = false
                    player.stop()
                    player.clearMediaItems()
                    return player
                }
            }
        }
        return null
    }

    /**
     * OPTIMIZAT: Eliminat complet Garbage Collection overhead în bucla de curățare
     */
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
                val desiredIndices =
                    listOf(centerIndex - 1, centerIndex, centerIndex + 1).filter { it >= 0 }

                val keyM1 = buildMapKey(scopeKey, centerIndex - 1)
                val keyC = buildMapKey(scopeKey, centerIndex)
                val keyP1 = buildMapKey(scopeKey, centerIndex + 1)

                val currentKeys =
                    indexToPlayer.keys.toTypedArray()
                for (key in currentKeys) {
                    if (key.startsWith("$scopeKey#") && key != keyM1 && key != keyC && key != keyP1) {
                        indexToPlayer.remove(key)?.let { player ->
                            indexToPostId.remove(key)
                            playerToKey.remove(player)
                            resetPlayerFull(player)
                            if (!pool.contains(player)) pool.addLast(player)
                        }
                    }
                }

                for (idx in desiredIndices) {
                    val currentKey = buildMapKey(scopeKey, idx)
                    val post = getPost(idx)

                    if (post == null) {
                        indexToPlayer.remove(currentKey)?.let { player ->
                            indexToPostId.remove(currentKey)
                            playerToKey.remove(player)
                            resetPlayerFull(player)
                            if (!pool.contains(player)) pool.addLast(player)
                        }
                        continue
                    }

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
                        continue
                    }

                    val player = existing ?: rentPlayerFromPool(scopeKey) ?: continue

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
                    playerToKey[player] = currentKey

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

    /**
     * OPTIMIZAT: Mutat asincron pe fundal controlul redării la scroll stabilizat
     */
    fun onPageSettled(scopeKey: String, centerIndex: Int, isScreenActive: Boolean) {
        if (!isScreenActive || activeScopeKey != scopeKey) return

        managerScope.launch(Dispatchers.Default) {
            val focusKey = buildMapKey(scopeKey, centerIndex)

            for ((key, player) in indexToPlayer) {
                if (key.startsWith("$scopeKey#")) {
                    val postId = indexToPostId[key]
                    val isUserPaused = postId != null && _userPausedPostIds.value.contains(postId)
                    val shouldPlay = (key == focusKey) && !isUserPaused

                    withContext(Dispatchers.Main.immediate) {
                        player.playWhenReady = shouldPlay
                        if (shouldPlay) {
                            if (!player.isPlaying) player.play()
                        } else {
                            player.pause()
                        }
                    }
                }
            }
        }
    }

    suspend fun currentPositionMs(scopeKey: String, postId: Int): Long? =
        withContext(Dispatchers.Main.immediate) {
            val key = indexToPostId.entries
                .firstOrNull { it.key.startsWith("$scopeKey#") && it.value == postId }
                ?.key ?: return@withContext null
            indexToPlayer[key]?.currentPosition
        }

    /**
     * OPTIMIZAT: Adăugat .clearVideoSurface() obligatoriu pentru a preveni memory leaks hardware
     */
    fun releaseScreenScope(scopeKey: String) {
        val currentKeys = indexToPlayer.keys.toTypedArray()
        for (key in currentKeys) {
            if (key.startsWith("$scopeKey#")) {
                indexToPlayer.remove(key)?.let { player ->
                    indexToPostId.remove(key)
                    playerToKey.remove(player) // ADD — înainte de pause(), din același motiv

                    player.playWhenReady = false
                    player.pause()
                    player.clearVideoSurface()

                    if (!pool.contains(player)) {
                        pool.addLast(player)
                    }
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
            _userPausedPostIds.update { it + postId }}
        else {
            _userPausedPostIds.update { it - postId }
            player.playWhenReady = true
            player.play()
        }
    }
}



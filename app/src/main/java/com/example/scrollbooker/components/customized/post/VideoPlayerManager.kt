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

    // SnapshotStateMap oferă reactivitate nativă în Jetpack Compose
    private val indexToPlayer: SnapshotStateMap<String, ExoPlayer> = mutableStateMapOf()
    private val indexToPostId: SnapshotStateMap<String, Int> = mutableStateMapOf()

    private val windowMutex = Mutex()
    private val managerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _userPausedPostIds = MutableStateFlow<Set<Int>>(emptySet())
    val userPausedPostIds: StateFlow<Set<Int>> = _userPausedPostIds.asStateFlow()

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

    /**
     * Oprește instant sunetul pentru ORICE alt ecran din fundal
     */
    fun activateScreenScope(scopeKey: String) {
        indexToPlayer.forEach { (key, player) ->
            if (!key.startsWith("$scopeKey#")) {
                player.playWhenReady = false
                player.pause()
            }
        }
    }

    /**
     * Gestionează fereastra glisantă (Sliding Window de 3) izolată pe contextul ecranului curent
     */
    fun ensureWindow(
        scopeKey: String,
        centerIndex: Int,
        isScreenActive: Boolean,
        getPost: (Int) -> Post?
    ) {
        managerScope.launch {
            windowMutex.withLock {
                val desiredIndices = listOf(centerIndex - 1, centerIndex, centerIndex + 1).filter { it >= 0 }
                val desiredKeys = desiredIndices.map { buildMapKey(scopeKey, it) }

                // 1. Eliminăm din map elementele care au ieșit din fereastra acestui ecran
                val toRemove = indexToPlayer.keys.filter { it.startsWith("$scopeKey#") && it !in desiredKeys }
                toRemove.forEach { key ->
                    indexToPlayer.remove(key)?.let { player ->
                        indexToPostId.remove(key)
                        player.playWhenReady = false
                        player.pause()
                        resetPlayer(player)
                        if (!pool.contains(player) && pool.size < maxPlayers) {
                            pool.addLast(player)
                        }
                    }
                }

                // 2. Alocăm sau actualizăm starea playerelor pentru indicii doriți
                desiredIndices.forEach { idx ->
                    val post = getPost(idx) ?: return@forEach
                    val currentKey = buildMapKey(scopeKey, idx)

                    val existing = indexToPlayer[currentKey]
                    val existingPostId = indexToPostId[currentKey]

                    if (existing != null && existingPostId == post.id) {
                        val isFocused = (idx == centerIndex)
                        val isUserPaused = _userPausedPostIds.value.contains(post.id)
                        existing.playWhenReady = isFocused && isScreenActive && !isUserPaused
                        if (!existing.playWhenReady) existing.pause()
                        return@forEach
                    }

                    // Extragere player din pool sau abandon dacă pool-ul e gol din cauza altor scurgeri
                    val player = existing ?: pool.removeFirstOrNull() ?: return@forEach

                    if (existing != null) {
                        resetPlayer(player)
                    }

                    indexToPlayer[currentKey] = player
                    indexToPostId[currentKey] = post.id

                    // Pregătire media asincronă
                    val mediaItem = MediaItem.fromUri(post.mediaFiles.first().url)
                    player.setMediaItem(mediaItem)
                    player.prepare()

                    val isFocused = (idx == centerIndex)
                    val isUserPaused = _userPausedPostIds.value.contains(post.id)
                    player.playWhenReady = isFocused && isScreenActive && !isUserPaused
                    if (!player.playWhenReady) player.pause()
                }
            }
        }
    }

    /**
     * Forțează oprirea redării la scroll (OnPageSettled)
     */
    fun onPageSettled(scopeKey: String, centerIndex: Int, isScreenActive: Boolean) {
        indexToPlayer.forEach { (key, player) ->
            if (key.startsWith("$scopeKey#")) {
                val idx = key.split("#").getOrNull(1)?.toIntOrNull() ?: -1
                val postId = indexToPostId[key]
                val isUserPaused = postId != null && _userPausedPostIds.value.contains(postId)

                val shouldPlay = (idx == centerIndex) && isScreenActive && !isUserPaused
                player.playWhenReady = shouldPlay
                if (!shouldPlay) player.pause()
            }
        }
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
        }
    }

    /**
     * REZOLVARE AUDIO FUNDAL: Oprește fizic elementele media dintr-un scope și le returnează în pool la distrugere
     */
    fun releaseScreenScope(scopeKey: String) {
        val keysToRemove = indexToPlayer.keys.filter { it.startsWith("$scopeKey#") }.toList()
        keysToRemove.forEach { key ->
            indexToPlayer.remove(key)?.let { player ->
                indexToPostId.remove(key)
                player.playWhenReady = false
                player.stop()
                player.clearMediaItems()
                resetPlayer(player)
                if (!pool.contains(player) && pool.size < maxPlayers) {
                    pool.addLast(player)
                }
            }
        }
    }

    private fun resetPlayer(player: ExoPlayer) {
        player.playWhenReady = false
        player.seekTo(0)
    }
}


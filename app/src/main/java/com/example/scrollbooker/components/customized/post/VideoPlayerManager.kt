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
    private val maxPlayers = 4
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

    /**
     * Schimbarea focusului între tab-uri sau ecrane (TikTok Style)
     */
    fun activateScreenScope(scopeKey: String) {
        activeScopeKey = scopeKey

        // Doar punem pe pauză elementele din fundal, NU le distrugem suprafața grafică preventiv!
        indexToPlayer.forEach { (key, player) ->
            if (!key.startsWith("$scopeKey#")) {
                player.playWhenReady = false
                indexToLastPosition[key] = player.currentPosition
                player.pause()
                // AM ELIMINAT player.clearVideoSurface() de aici!
                if (!pool.contains(player)) {
                    pool.addLast(player)
                }
            }
        }
    }

    fun freezeScreenScope(scopeKey: String) {
        indexToPlayer.forEach { (key, player) ->
            if (key.startsWith("$scopeKey#")) {
                player.playWhenReady = false
                indexToLastPosition[key] = player.currentPosition
                player.pause()
            }
        }
    }

    /**
     * Sliding Window stabil care nu lasă pool-ul să se golească
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

        activeScopeKey = scopeKey

        managerScope.launch {
            windowMutex.withLock {
                val desiredIndices = listOf(centerIndex - 1, centerIndex, centerIndex + 1).filter { it >= 0 }
                val desiredKeys = desiredIndices.map { buildMapKey(scopeKey, it) }

                // 1. Curățăm elementele care au ieșit definitiv din fereastră prin scroll agresiv
                val toRemove = indexToPlayer.keys.filter { it.startsWith("$scopeKey#") && it !in desiredKeys }
                toRemove.forEach { key ->
                    indexToPlayer.remove(key)?.let { player ->
                        indexToPostId.remove(key)
                        indexToLastPosition.remove(key)
                        resetPlayerFull(player) // Curățare totală la scroll complet în afara ferestrei
                        if (!pool.contains(player)) {
                            pool.addLast(player)
                        }
                    }
                }

                // 2. Alocăm sau recuperăm playerele pentru indicii vizibili
                desiredIndices.forEach { idx ->
                    val post = getPost(idx) ?: return@forEach
                    val currentKey = buildMapKey(scopeKey, idx)

                    val existing = indexToPlayer[currentKey]
                    val existingPostId = indexToPostId[currentKey]

                    // TIPARUL TIKTOK REVENIRE: Dacă playerul există deja pe această cheie unică, dăm doar PLAY direct!
                    if (existing != null && existingPostId == post.id) {
                        val isFocused = (idx == centerIndex)
                        val isUserPaused = _userPausedPostIds.value.contains(post.id)

                        existing.playWhenReady = isFocused && !isUserPaused
                        if (isFocused && !isUserPaused && !existing.isPlaying) {
                            existing.play()
                        } else if (!existing.playWhenReady) {
                            existing.pause()
                        }
                        return@forEach
                    }

                    // PULL DIN POOL: Dacă ecranul curent are nevoie de un player nou, îl trage din resurse
                    val player = existing ?: pool.removeFirstOrNull() ?: return@forEach

                    indexToPlayer[currentKey] = player
                    indexToPostId[currentKey] = post.id

                    // REZOLVARE BLOCAJ SWIPE: Dacă este un player proaspăt tras din pool (existing == null),
                    // trebuie să îi dăm o resetare hardware atomică înainte de a-i injecta noul URL media!
                    if (existing == null) {
                        player.playWhenReady = false
                        player.clearVideoSurface() // Dezlipim vechea textură de pe alt ecran ca să nu înghețe
                        player.stop()
                        player.clearMediaItems()
                        player.seekTo(0)
                    } else {
                        resetPlayerFull(player)
                    }

                    // Preluăm poziția salvată în cache dacă utilizatorul a mai fost pe acest video
                    val savedPosition = indexToLastPosition[currentKey] ?: 0L

                    val mediaItem = MediaItem.fromUri(post.mediaFiles.first().url)
                    player.setMediaItem(mediaItem)
                    player.prepare()

                    if (savedPosition > 0L) {
                        player.seekTo(savedPosition)
                    }

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
                if (!shouldPlay) player.pause()
            }
        }
    }

    fun releaseScreenScope(scopeKey: String) {
        val keysToRemove = indexToPlayer.keys.filter { it.startsWith("$scopeKey#") }.toList()
        keysToRemove.forEach { key ->
            indexToPlayer.remove(key)?.let { player ->
                indexToPostId.remove(key)
                indexToLastPosition.remove(key)
                resetPlayerFull(player)
                if (!pool.contains(player)) {
                    pool.addLast(player)
                }
            }
        }
    }

    private fun resetPlayerFull(player: ExoPlayer) {
        player.playWhenReady = false
        player.stop()
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
        }
    }
}


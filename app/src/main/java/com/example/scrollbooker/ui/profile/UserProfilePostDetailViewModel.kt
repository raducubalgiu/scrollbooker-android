package com.example.scrollbooker.ui.profile

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.plus
import kotlin.collections.set

@HiltViewModel
class UserProfilePostDetailViewModel @Inject constructor(
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unBookmarkPostUseCase: UnBookmarkPostUseCase,
    @ApplicationContext private val app: Context,
): ViewModel() {
    // Post Action
    private val _postUi = MutableStateFlow<Map<Int, PostActionUiState>>(emptyMap())
    fun observePostUi(postId: Int): StateFlow<PostActionUiState> =
        _postUi.map { it[postId] ?: PostActionUiState.EMPTY }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PostActionUiState.EMPTY)

    private inline fun MutableStateFlow<Map<Int, PostActionUiState>>.edit(
        postId: Int,
        crossinline reducer: (PostActionUiState) -> PostActionUiState
    ) {
        update { map ->
            val curr = map[postId] ?: PostActionUiState.EMPTY
            map + (postId to reducer(curr))
        }
    }

    private inline fun toggleAction(
        postId: Int,
        backendFlag: Boolean,
        backendCount: Int,
        crossinline isFlagOverridden: (PostActionUiState) -> Boolean?,
        crossinline setFlag: (PostActionUiState, Boolean) -> PostActionUiState,
        crossinline savingOn: (PostActionUiState) -> PostActionUiState,
        crossinline savingOff: (PostActionUiState) -> PostActionUiState,
        crossinline getDelta: (PostActionUiState) -> Int,
        crossinline setDelta: (PostActionUiState, Int) -> PostActionUiState,
        crossinline doOn: suspend () -> Result<Unit>,
        crossinline doOff: suspend () -> Result<Unit>,
    ) {
        val currentPostAction = _postUi.value[postId] ?: PostActionUiState.EMPTY

        val currentFlag = isFlagOverridden(currentPostAction) ?: backendFlag

        val wantOn = !currentFlag

        _postUi.edit(postId) { s ->
            val base = backendCount
            val curr = base + getDelta(s)
            val next = if(wantOn) curr + 1 else curr - 1

            savingOn(setFlag(setDelta(s, next - base), wantOn))
        }

        viewModelScope.launch {
            val result = if(wantOn) doOn() else doOff()

            if(result.isSuccess) {
                _postUi.edit(postId) { s -> savingOff(s) }
            } else {
                _postUi.edit(postId) { s ->
                    val revertedFlag = !(isFlagOverridden(s)!!)
                    val reverted = setFlag(setDelta(s, 0), revertedFlag)
                    savingOff(reverted)
                }
            }
        }
    }

    fun toggleLike(post: Post) {
        toggleAction(
            postId = post.id,
            backendFlag = post.userActions.isLiked,
            backendCount = post.counters.likeCount,
            isFlagOverridden = { it.isLiked },
            setFlag = { s, v -> s.copy(isLiked = v) },
            savingOn = { it.copy(isSavingLike = true) },
            savingOff = { it.copy(isSavingLike = false) },
            getDelta = { it.likesCount },
            setDelta = { s, d -> s.copy(likesCount = d) },
            doOn = { likePostUseCase(post.id) },
            doOff = { unLikePostUseCase(post.id) }
        )
    }

    fun toggleBookmark(post: Post) {
        toggleAction(
            postId = post.id,
            backendFlag = post.userActions.isBookmarked,
            backendCount = post.counters.bookmarkCount,
            isFlagOverridden = { it.isBookmarked },
            setFlag = { s, v -> s.copy(isBookmarked = v) },
            savingOn = { it.copy(isSavingBookmark = true) },
            savingOff = { it.copy(isSavingBookmark = false) },
            getDelta = { it.bookmarksCount },
            setDelta = { s, d -> s.copy(bookmarksCount = d) },
            doOn = { bookmarkPostUseCase(post.id) },
            doOff = { unBookmarkPostUseCase(post.id) }
        )
    }

    // Player
    private val maxPlayers = 3
    private val pool = ArrayDeque<ExoPlayer>(maxPlayers)
    private val indexToPlayer: SnapshotStateMap<Int, ExoPlayer> = mutableStateMapOf()
    private val indexToPostId: SnapshotStateMap<Int, Int> = mutableStateMapOf()

    private var focusedIndex: Int? = null
    private val windowMutex = Mutex()

    init {
        repeat(maxPlayers) { pool.add(createPlayer(app)) }
    }

    @androidx.annotation.OptIn(UnstableApi::class)
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

    @androidx.annotation.OptIn(UnstableApi::class)
    @OptIn(UnstableApi::class)
    private fun createPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setLoadControl(createLoadControl())
            .setHandleAudioBecomingNoisy(true)
            .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(app.applicationContext)))
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
                volume = 1f
            }
    }

    fun ensureWindow(
        centerIndex: Int,
        getPost: (Int) -> Post?
    ) {
        viewModelScope.launch {
            windowMutex.withLock {
                ensureWindowInternal(centerIndex, getPost)
            }
        }
    }

    private fun ensureWindowInternal(
        centerIndex: Int,
        getPost: (Int) -> Post?
    ) {
        val desired = listOf(centerIndex - 1, centerIndex, centerIndex + 1)
            .filter { it >= 0 }

        // 1) Detach indices care nu mai sunt în fereastră
        val toRemove = indexToPlayer.keys - desired
        toRemove.forEach { idx ->
            indexToPlayer.remove(idx)?.let { player ->
                indexToPostId.remove(idx)
                resetPlayer(player)
                pool.addLast(player)
            }
        }

        // 2) Attach indices noi
        desired.forEach { idx ->
            if (idx < 0) return@forEach
            val post = getPost(idx) ?: return@forEach

            val existing = indexToPlayer[idx]
            val existingPostId = indexToPostId[idx]
            if (existing != null && existingPostId == post.id) return@forEach

            val player = existing ?: pool.removeFirstOrNull() ?: return@forEach

            // dacă era “existing” dar alt post, îl resetăm
            if (existing != null) {
                resetPlayer(player)
            }

            indexToPlayer[idx] = player
            indexToPostId[idx] = post.id

            prepareForPost(player, post)

            player.playWhenReady = (idx == focusedIndex)
        }

        applyFocus(centerIndex)
    }

    fun ensureImmediate(
        centerIndex: Int,
        getPost: (Int) -> Post?
    ) {
        if(!windowMutex.tryLock()) return
        try {
            ensureWindowInternal(centerIndex, getPost)
        } finally {
            windowMutex.unlock()
        }
    }

    fun onPageSettled(index: Int) {
        focusedIndex = index
        applyFocus(index)
    }

    private fun applyFocus(index: Int) {
        indexToPlayer.forEach { (idx, player) ->
            val shouldPlay = (idx == index)
            player.playWhenReady = shouldPlay
            player.volume = if (shouldPlay) 1f else 0f
            if (!shouldPlay) player.pause()
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
        player.pause()
        player.stop()
        player.clearMediaItems()
        player.seekTo(0)
        player.volume = 0f
    }

    fun seekToZero(index: Int) {
        val player = getPlayerForIndex(index) ?: return
        player.seekTo(0)
    }

    fun getPlayerForIndex(index: Int): ExoPlayer? = indexToPlayer[index]

    fun togglePlayer(index: Int) {
        val player = getPlayerForIndex(index) ?: return

        val isFocused = (index == focusedIndex)

        if(player.isPlaying) {
            player.playWhenReady = false

        } else {
            if(isFocused) {
                player.playWhenReady = true
            } else {
                focusedIndex = index
                applyFocus(index)
            }
        }
    }

    fun stopDetailSession() {
        indexToPlayer.values.forEach { player ->
            player.pause()
        }
    }

    override fun onCleared() {
        stopDetailSession()
        super.onCleared()
    }
}
package com.example.scrollbooker.ui.feed
import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateMapOf
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
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.VideoPlayerCache
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomainsWithBusinessTypes
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.useCase.GetAllBusinessDomainsWithBusinessTypesUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetExplorePostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetFollowingPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val getFollowingPostsUseCase: GetFollowingPostsUseCase,
    private val getAllBusinessDomainsWithBusinessTypesUseCase: GetAllBusinessDomainsWithBusinessTypesUseCase,
    private val getExplorePostsUseCase: GetExplorePostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unBookmarkPostUseCase: UnBookmarkPostUseCase,
    @ApplicationContext private val application: Context,
) : ViewModel() {
    // Drawer
    private val _businessDomainsWithBusinessTypes =
        MutableStateFlow<FeatureState<List<BusinessDomainsWithBusinessTypes>>>(FeatureState.Loading)

    val businessDomainsWithBusinessTypes: StateFlow<FeatureState<List<BusinessDomainsWithBusinessTypes>>> =
        _businessDomainsWithBusinessTypes.asStateFlow()

    private val _selectedBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val selectedBusinessTypes: StateFlow<Set<Int>> = _selectedBusinessTypes

    fun setSelectedBusinessTypes(businessTypes: Set<Int>) {
        _selectedBusinessTypes.value = businessTypes
    }

    // Feed
    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    val explorePosts: Flow<PagingData<Post>> = selectedBusinessTypes
        .map { it.toList() }
        .flatMapLatest { selectedTypes -> getExplorePostsUseCase(selectedTypes) }
        .cachedIn(viewModelScope)

    private val _followingPosts: Flow<PagingData<Post>> by lazy {
        getFollowingPostsUseCase()
            .cachedIn(viewModelScope)
    }
    val followingPosts: Flow<PagingData<Post>> get() = _followingPosts

    enum class FeedTab { Explore, Following }

    class TabPlaybackState {
        val indexToPlayer = mutableStateMapOf<Int, ExoPlayer>()
        val indexToPostId = mutableStateMapOf<Int, Int>()

        var focusedIndex by mutableStateOf<Int?>(null)
        var lastSettledPostId by mutableStateOf<Int?>(null)

        var userPausedPostId by mutableStateOf<Int?>(null)

        val autoPausedByDrawer = mutableSetOf<Int>()
    }

    // Player
    private val tabStates: MutableMap<FeedTab, TabPlaybackState> = mutableMapOf(
        FeedTab.Explore to TabPlaybackState(),
        FeedTab.Following to TabPlaybackState()
    )

    private val _activeTab = MutableStateFlow<FeedTab>(FeedTab.Explore)
    val activeTab: StateFlow<FeedTab> = _activeTab.asStateFlow()

    private val maxPlayers = 3
    private val pool = ArrayDeque<ExoPlayer>(maxPlayers)
    private val windowMutex = Mutex()

    init {
        repeat(maxPlayers) { pool.add(createPlayer(application)) }
        loadAllBusinessDomainsWithBusinessTypes()
    }


    fun setActiveTab(newTab: FeedTab) {
        val oldTab = _activeTab.value
        if(oldTab == newTab) return

        resetSession(oldTab)
        _activeTab.value = newTab
    }

    fun resetSession(tab: FeedTab) {
        val state = tabStates.getValue(tab)

        state.indexToPlayer.values.forEach { p ->
            resetPlayer(p)
            pool.addLast(p)
        }
        state.indexToPlayer.clear()
        state.indexToPostId.clear()

        state.focusedIndex = null
        state.lastSettledPostId = null
        state.userPausedPostId = null
        state.autoPausedByDrawer.clear()
    }

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

    @OptIn(UnstableApi::class)
    private fun createPlayer(context: Context): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setLoadControl(createLoadControl())
            .setHandleAudioBecomingNoisy(true)
            .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(application.applicationContext)))
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
        tab: FeedTab,
        centerIndex: Int,
        getPost: (Int) -> Post?
    ) {
        viewModelScope.launch {
            windowMutex.withLock {
                ensureWindowInternal(tab, centerIndex, getPost)
            }
        }
    }

    private fun ensureWindowInternal(tab: FeedTab, centerIndex: Int, getPost: (Int) -> Post?) {
        val state = tabStates.getValue(tab)

        val desired = listOf(centerIndex - 1, centerIndex, centerIndex + 1).filter { it >= 0 }

        val toRemove = state.indexToPlayer.keys - desired.toSet()
        toRemove.forEach { idx ->
            state.indexToPlayer.remove(idx)?.let { p ->
                state.indexToPostId.remove(idx)
                resetPlayer(p)
                pool.addLast(p)
            }
        }

        desired.forEach { idx ->
            val post = getPost(idx) ?: return@forEach
            val existingPlayer = state.indexToPlayer[idx]
            val existingPostId = state.indexToPostId[idx]
            if (existingPlayer != null && existingPostId == post.id) return@forEach

            val player = existingPlayer ?: pool.removeFirstOrNull() ?: return@forEach
            if (existingPlayer != null) resetPlayer(player)

            state.indexToPlayer[idx] = player
            state.indexToPostId[idx] = post.id

            prepareForPost(player, post)

            val isFocused = idx == state.focusedIndex
            val isUserPaused = state.userPausedPostId == post.id

            player.playWhenReady = isFocused && !isUserPaused && _activeTab.value == tab
            player.volume = if (player.playWhenReady) 1f else 0f
            if (!player.playWhenReady) player.pause()
        }

        if (_activeTab.value == tab) applyFocus(tab, centerIndex)
    }

    fun onPageSettled(tab: FeedTab, index: Int, postId: Int) {
        val state = tabStates.getValue(tab)

        val changedPost = state.lastSettledPostId != null && state.lastSettledPostId != postId

        if (changedPost) {
            state.userPausedPostId = null
        }

        state.lastSettledPostId = postId
        state.focusedIndex = index

        if (_activeTab.value == tab) applyFocus(tab, index)
    }

    private fun applyFocus(tab: FeedTab, index: Int) {
        val state = tabStates.getValue(tab)

        state.indexToPlayer.forEach { (idx, player) ->
            val postId = state.indexToPostId[idx]
            val isUserPaused = postId != null && state.userPausedPostId == postId
            val shouldPlay = (_activeTab.value == tab) && (idx == index) && !isUserPaused

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

    fun getPlayerForIndex(tab: FeedTab, index: Int): ExoPlayer? =
        tabStates[tab]?.indexToPlayer?.get(index)

    fun userPausedPostId(tab: FeedTab): StateFlow<Int?> =
        activeTab
            .map { tabStates[it]?.userPausedPostId }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun togglePlayer(tab: FeedTab, index: Int) {
        val state = tabStates.getValue(tab)
        val player = state.indexToPlayer[index] ?: return
        val postId = state.indexToPostId[index] ?: return

        val isFocused = state.focusedIndex == index && _activeTab.value == tab

        if (player.isPlaying) {
            player.pause()
            player.playWhenReady = false
            state.userPausedPostId = postId
        } else {
            state.userPausedPostId = null
            if (isFocused) {
                player.volume = 1f
                player.playWhenReady = true
            } else {
                state.focusedIndex = index
                applyFocus(tab, index)
            }
        }
    }

    fun pauseIfPlaying(tab: FeedTab, index: Int) {
        val state = tabStates.getValue(tab)
        val player = state.indexToPlayer[index] ?: return

        if (player.isPlaying) {
            state.autoPausedByDrawer.add(index)
            player.playWhenReady = false
            player.pause()
            player.volume = 0f
        }
    }

    fun resumeAfterDrawer(tab: FeedTab, index: Int) {
        val state = tabStates.getValue(tab)
        if (!state.autoPausedByDrawer.contains(index)) return

        val postId = state.indexToPostId[index] ?: return
        val isUserPaused = state.userPausedPostId == postId

        if (_activeTab.value == tab && state.focusedIndex == index && !isUserPaused) {
            state.indexToPlayer[index]?.apply {
                volume = 1f
                playWhenReady = true
            }
        }
    }

    fun stopDetailSession() {
        tabStates.values.forEach { state ->
            state.indexToPlayer.values.forEach { player ->
                player.playWhenReady = false
                player.pause()
                player.volume = 0f
            }
        }
    }

    override fun onCleared() {
        stopDetailSession()

        pool.forEach { it.release() }
        pool.clear()

        super.onCleared()
    }

    private val _showBottomBar = MutableStateFlow<Boolean>(true)
    val showBottomBar: StateFlow<Boolean> = _showBottomBar.asStateFlow()

    fun toggleBottomBar() { _showBottomBar.value = !_showBottomBar.value }

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
            getDelta = { it.likeCountDelta },
            setDelta = { s, d -> s.copy(likeCountDelta = d) },
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
            getDelta = { it.bookmarkCountDelta },
            setDelta = { s, d -> s.copy(bookmarkCountDelta = d) },
            doOn = { bookmarkPostUseCase(post.id) },
            doOff = { unBookmarkPostUseCase(post.id) }
        )
    }

    private fun loadAllBusinessDomainsWithBusinessTypes() {
        viewModelScope.launch {
            _businessDomainsWithBusinessTypes.value = FeatureState.Loading
            _businessDomainsWithBusinessTypes.value = getAllBusinessDomainsWithBusinessTypesUseCase()
        }
    }

//    // Player
//    private val playerPool = mutableMapOf<Int, ExoPlayer>()
//    private val _playerStates = mutableMapOf<Int, MutableStateFlow<PlayerUIState>>()
//
//    private var playerThread: HandlerThread = HandlerThread("ExoPlayer Thread", Process.THREAD_PRIORITY_AUDIO)
//        .apply { start() }
//
//    private val _currentPost = MutableStateFlow<Post?>(null)
//    val currentPost: StateFlow<Post?> = _currentPost.asStateFlow()
//
//    @OptIn(UnstableApi::class)
//    private fun createLoadControl(): DefaultLoadControl {
//        return DefaultLoadControl.Builder()
//            .setBufferDurationsMs(
//                1500,
//                5000,
//                500,
//                1500
//            )
//            .setTargetBufferBytes(C.LENGTH_UNSET)
//            .setPrioritizeTimeOverSizeThresholds(true)
//            .build()
//    }
//
//    private val playerListeners = mutableMapOf<Int, Player.Listener>()
//
//    private fun attachPlayerStateListener(postId: Int, player: ExoPlayer) {
//        val listener = object : Player.Listener {
//            override fun onIsPlayingChanged(isPlaying: Boolean) {
//                updatePlayerState(postId) { it.copy(
//                    isPlaying = isPlaying,
//                    hasStartedPlayback = isPlaying || it.hasStartedPlayback)
//                }
//            }
//
//            override fun onPlaybackStateChanged(state: Int) {
//                updatePlayerState(postId) { it.copy(
//                    isReady = state == Player.STATE_READY,
//                    isBuffering = state == Player.STATE_BUFFERING
//                )}
//            }
//
//            override fun onRenderedFirstFrame() {
//                updatePlayerState(postId) { it.copy(
//                    isFirstFrameRendered = true
//                )}
//            }
//        }
//
//        player.addListener(listener)
//        playerListeners[postId] = listener
//    }
//
//    fun getPlayerState(postId: Int): StateFlow<PlayerUIState> {
//        return _playerStates.getOrPut(postId) { MutableStateFlow(PlayerUIState()) }
//    }
//
//    private fun updatePlayerState(postId: Int, transform: (PlayerUIState) -> PlayerUIState) {
//        _playerStates[postId]?.let { mutableFlow ->
//            mutableFlow.value = transform(mutableFlow.value)
//        }
//    }
//
//    fun initializePlayer(
//        post: Post?,
//        previousPost: Post?,
//        nextPost: Post?,
//    ) {
//        if (post == null) return
//        _currentPost.value = post
//
//        val player = getOrCreatePlayer(post)
//
//        val newMediaItem = MediaItem.fromUri(post.mediaFiles.first().url ?: "")
//        val sameItem = player.currentMediaItem?.localConfiguration?.uri == newMediaItem.localConfiguration?.uri
//
//        if(!sameItem) {
//            player.setMediaItem(newMediaItem)
//            player.prepare()
//            player.seekTo(0)
//        }
//        player.playWhenReady = true
//
//        resetInactivePlayerStates(post.id)
//        preloadVideo(previousPost)
//        preloadVideo(nextPost)
//    }
//
//    private fun resetInactivePlayerStates(postId: Int) {
//        _playerStates
//            .filterKeys { it != postId }
//            .forEach { (_, state) ->
//                state.value = PlayerUIState()
//            }
//    }
//
//    private fun preloadVideo(post: Post?) {
//        if(post == null) return
//
//        val player = getOrCreatePlayer(post)
//        val mediaItem = MediaItem.fromUri(post.mediaFiles.first().url ?: "")
//
//        if(player.currentMediaItem?.localConfiguration?.uri == mediaItem.localConfiguration?.uri) return
//
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.playWhenReady = false
//    }
//
//    @OptIn(UnstableApi::class)
//    fun getOrCreatePlayer(post: Post): ExoPlayer {
//        return playerPool.getOrPut(post.id) {
//            ExoPlayer.Builder(application.applicationContext)
//                .setLoadControl(createLoadControl())
//                .setPlaybackLooper(playerThread.looper)
//                .setMediaSourceFactory(DefaultMediaSourceFactory(VideoPlayerCache.getFactory(application.applicationContext)))
//                .setHandleAudioBecomingNoisy(true)
//                .setAudioAttributes(
//                    AudioAttributes.Builder()
//                        .setUsage(C.USAGE_MEDIA)
//                        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
//                        .build(), true
//                )
//                .build()
//                .also {
//                    it.repeatMode = ExoPlayer.REPEAT_MODE_ONE
//                    it.playWhenReady = false
//                    attachPlayerStateListener(post.id, it)
//                }
//        }
//    }
//
//    fun togglePlayer(postId: Int) {
//        val player = playerPool[postId] ?: return
//
//        if(player.isPlaying) {
//            player.pause()
//            player.playWhenReady = false
//        } else {
//            player.playWhenReady = true
//        }
//    }
//
//    fun pauseIfPlaying(postId: Int) {
//        val player = playerPool[postId]
//        if(player?.isPlaying == true) {
//            player.pause()
//            player.playWhenReady = false
//        }
//    }
//
//    fun resumeIfPlaying(postId: Int) {
//        val player = playerPool[postId]
//        if(player != null && !player.isPlaying) {
//            player.playWhenReady = true
//        }
//    }
//
//    fun pauseUnusedPlayers(visiblePostId: Int) {
//        playerPool.forEach { (postId, player) ->
//            if(postId != visiblePostId) {
//                player.playWhenReady = false
//                player.pause()
//            }
//        }
//    }
//
//    private fun limitPlayerPoolSize(postId: Int) {
//        if(playerPool.size > 5) {
//            playerPool.entries
//                .filter { it.key != postId }
//                .take(playerPool.size - 5)
//                .forEach {
//                    Timber.d("Release player for postId: $postId")
//                    it.value.release()
//                    playerPool.remove(it.key)
//                }
//        }
//    }
//
//    fun releasePlayer(postId: Int?) {
//        postId?.let { id ->
//            playerPool[id]?.apply {
//                playWhenReady = false
//                pause()
//            }
//
//            resetInactivePlayerStates(postId)
//            limitPlayerPoolSize(postId)
//        }
//    }
//
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
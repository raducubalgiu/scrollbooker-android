package com.example.scrollbooker.ui.feed
import android.content.Context
import android.os.HandlerThread
import android.os.Process
import androidx.annotation.OptIn
import androidx.compose.runtime.State
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
import com.example.scrollbooker.ui.shared.posts.PlayerUIState
import com.example.scrollbooker.ui.shared.posts.PostActionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

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

    private val _isFiltering = MutableStateFlow<Boolean>(false)
    val isFiltering: StateFlow<Boolean> = _isFiltering.asStateFlow()

    fun setSelectedBusinessTypes(businessTypes: Set<Int>) {
        _selectedBusinessTypes.value = businessTypes
    }

    fun setIsFiltering(isFiltering: Boolean) {
        _isFiltering.value = isFiltering
    }

    // Feed
    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    val explorePosts: Flow<PagingData<Post>> = selectedBusinessTypes
        .map { it.toList() }
        .flatMapLatest { selectedTypes ->
            val filteringSnapshot = _isFiltering.value

            getExplorePostsUseCase(
                selectedBusinessTypes = selectedTypes,
                isFiltering = filteringSnapshot
            )
            .onEach {
                if(filteringSnapshot) _isFiltering.value = false
            }
        }
        .cachedIn(viewModelScope)

    private val _followingPosts: Flow<PagingData<Post>> by lazy {
        getFollowingPostsUseCase()
            .cachedIn(viewModelScope)
    }
    val followingPosts: Flow<PagingData<Post>> get() = _followingPosts

    // Player
    private val maxPlayers = 3
    private val pool = ArrayDeque<ExoPlayer>(maxPlayers)
    private val indexToPlayer: SnapshotStateMap<Int, ExoPlayer> = mutableStateMapOf()
    private val indexToPostId: SnapshotStateMap<Int, Int> = mutableStateMapOf()

    private var focusedIndex: Int? = null
    private val windowMutex = Mutex()

    @Volatile private var generation: Long = 0L
    private var ensureJob: Job? = null

    private val _userPausedPostIds = MutableStateFlow<Set<Int>>(emptySet())
    val userPausedPostIds: StateFlow<Set<Int>> = _userPausedPostIds.asStateFlow()

    private val autoPausedByDrawer = mutableSetOf<Int>()

    init {
        viewModelScope.launch {
            repeat(maxPlayers) { pool.add(createPlayer(application)) }
            loadAllBusinessDomainsWithBusinessTypes()
        }
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
        centerIndex: Int,
        getPost: (Int) -> Post?
    ) {
        val myGen = generation
        ensureJob?.cancel()
        ensureJob = viewModelScope.launch {
            windowMutex.withLock {
                if (myGen != generation) return@withLock
                ensureWindowInternal(myGen, centerIndex, getPost)
            }
        }
    }

    private fun ensureWindowInternal(
        myGen: Long,
        centerIndex: Int,
        getPost: (Int) -> Post?
    ) {
        if (myGen != generation) return

        val desired = listOf(centerIndex - 1, centerIndex, centerIndex + 1)
            .filter { it >= 0 }

        val toRemove = indexToPlayer.keys - desired
        toRemove.forEach { idx ->
            indexToPlayer.remove(idx)?.let { player ->
                indexToPostId.remove(idx)
                resetPlayer(player)
                pool.addLast(player)
            }
        }

        desired.forEach { idx ->
            if (idx < 0) return@forEach
            val post = getPost(idx) ?: return@forEach

            val existing = indexToPlayer[idx]
            val existingPostId = indexToPostId[idx]
            if (existing != null && existingPostId == post.id) return@forEach

            val player = existing ?: pool.removeFirstOrNull() ?: return@forEach

            if (existing != null) {
                resetPlayer(player)
            }

            indexToPlayer[idx] = player
            indexToPostId[idx] = post.id

            prepareForPost(player, post)

            val isFocused = (idx == focusedIndex)
            val isUserPaused = _userPausedPostIds.value.contains(post.id)

            player.playWhenReady = isFocused && !isUserPaused
            player.volume = if(isFocused && !isUserPaused) 1f else 0f

            if(!isFocused || isUserPaused) player.pause()
        }

        applyFocus(centerIndex)
    }

    fun onPageSettled(index: Int) {
        focusedIndex = index
        applyFocus(index)
    }

    private fun applyFocus(index: Int) {
        indexToPlayer.forEach { (idx, player) ->
            val postId = indexToPostId[idx]
            val isUserPaused = postId != null && _userPausedPostIds.value.contains(postId)
            val shouldPlay = (idx == index) && !isUserPaused

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

    fun getPlayerForIndex(index: Int): ExoPlayer? = indexToPlayer[index]

    fun togglePlayer(index: Int) {
        val player = getPlayerForIndex(index) ?: return
        val postId = indexToPostId[index] ?: return

        val isFocused = (index == focusedIndex)

        if(player.isPlaying) {
            player.pause()
            player.playWhenReady = false
            _userPausedPostIds.update { it + postId }
        } else {
            _userPausedPostIds.update { it - postId }

            if(isFocused) {
                player.volume = 1f
                player.playWhenReady = true
            } else {
                focusedIndex = index
                applyFocus(index)
            }
        }
    }

    fun pauseIfPlaying(index: Int) {
        val player = getPlayerForIndex(index)
        if(player?.isPlaying == true) {
            autoPausedByDrawer.add(index)
            player.pause()
            player.playWhenReady = false
        }
    }

    fun resumeIfAllowed(index: Int) {
        val player = getPlayerForIndex(index) ?: return
        val postId = indexToPostId[index] ?: return
        val isUserPaused = userPausedPostIds.value.contains(postId)

        if(index == focusedIndex && !isUserPaused) {
            player.playWhenReady = true
        }
    }

    fun resumeAfterDrawer(index: Int) {
        if(!autoPausedByDrawer.remove(index)) return
        resumeIfAllowed(index)
    }

    fun pauseAllNow() {
        generation++
        ensureJob?.cancel()
        val players = indexToPlayer.values.toList()
        viewModelScope.launch(Dispatchers.Main.immediate) {
            players.forEach { p ->
                p.playWhenReady = false
                p.volume = 0f
                p.pause()
            }
        }
    }

    fun stopDetailSession() {
        indexToPlayer.values.forEach { player ->
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onCleared() {
        stopDetailSession()
        super.onCleared()
    }

    // Actions
    private val _currentByTab = MutableStateFlow<Map<Int, Post?>>(emptyMap())
    val currentByTab: StateFlow<Map<Int, Post?>> = _currentByTab.asStateFlow()

    fun currentPost(tab: Int): StateFlow<Post?> =
        currentByTab
            .map { it[tab] }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun updateCurrentPost(tab: Int, post: Post?) {
        _currentByTab.update { it + (tab to post) }
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
}
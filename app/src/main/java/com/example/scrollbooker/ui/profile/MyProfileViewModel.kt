package com.example.scrollbooker.ui.profile

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.repost.domain.useCase.GetUserRepostsUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateBioUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateFullNameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateGenderUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdatePublicEmailUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateUsernameUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.UpdateWebsiteUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

enum class PostTabEnum {
    MY_POSTS,
    REPOSTS,
    BOOKMARKS
}

data class SelectedPostUi(
    val postId: Int,
    val tab: PostTabEnum
)

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val updateFullNameUseCase: UpdateFullNameUseCase,
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val updateBioUseCase: UpdateBioUseCase,
    private val updateGenderUseCase: UpdateGenderUseCase,
    private val updateWebsiteUseCase: UpdateWebsiteUseCase,
    private val updatePublicEmailUseCase: UpdatePublicEmailUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    private val getUserRepostsUseCase: GetUserRepostsUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val authDataStore: AuthDataStore,
    @ApplicationContext private val app: Context,
): ViewModel() {
    private val _userProfileState =
        MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val userProfileState: StateFlow<FeatureState<UserProfile>> = _userProfileState

    private val _initCompleted = MutableStateFlow(false)
    val isInitLoading = combine(_userProfileState, _initCompleted) { profile, done ->
        (profile is FeatureState.Loading || !done)
    }.stateIn(viewModelScope, SharingStarted.Companion.Eagerly, true)

    private fun loadUserProfile() {
        viewModelScope.launch {
            val userId = authDataStore.getUserId().firstOrNull()

            if(userId == null) {
                Timber.Forest.tag("Refetch UserProfile").e("ERROR: on Refetching User Profile. User Id not found ")
                throw IllegalStateException("User id not found in datastore")
            }

            _userProfileState.value = FeatureState.Loading

            val response = withVisibleLoading { getUserProfileUseCase(userId, lat = null, lng = null) }

            _userProfileState.value = response
        }
    }

    init {
        loadUserProfile()
    }

    // Tabs
    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    val userPosts: Flow<PagingData<Post>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .onEach { _initCompleted.value = true }
        .cachedIn(viewModelScope)

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    val employees: Flow<PagingData<Employee>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId -> getEmployeesByOwnerUseCase(userId) }
        .cachedIn(viewModelScope)

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    val userReposts: Flow<PagingData<Post>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId ->
            getUserRepostsUseCase(userId)
        }
        .cachedIn(viewModelScope)

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    val userBookmarkedPosts: Flow<PagingData<Post>> = authDataStore.getUserId()
        .filterNotNull()
        .flatMapLatest { userId ->
            getUserBookmarkedPostsUseCase(userId)
        }
        .cachedIn(viewModelScope)

    // Selected Post
    private val _selectedPost = MutableStateFlow<SelectedPostUi?>(null)
    val selectedPost: StateFlow<SelectedPostUi?> = _selectedPost.asStateFlow()

    private val _currentPost = MutableStateFlow<Post?>(null)
    val currentPost: StateFlow<Post?> = _currentPost.asStateFlow()

    fun setSelectedPost(selectedPost: SelectedPostUi) {
        _selectedPost.value = selectedPost
    }

    @kotlin.OptIn(ExperimentalCoroutinesApi::class)
    val detailPostsFlow: Flow<PagingData<Post>> =
        selectedPost
            .filterNotNull()
            .map { it.tab }
            .distinctUntilChanged()
            .flatMapLatest { tab ->
                when (tab) {
                    PostTabEnum.MY_POSTS -> userPosts
                    PostTabEnum.REPOSTS -> userReposts
                    PostTabEnum.BOOKMARKS -> userBookmarkedPosts
                }
            }

    // Player
    private val maxPlayers = 3
    private val pool = ArrayDeque<ExoPlayer>(maxPlayers)
    private val indexToPlayer = mutableMapOf<Int, ExoPlayer>()
    private val indexToPostId = mutableMapOf<Int, Int>()

    private var focusedIndex: Int? = null
    private val windowMutex = Mutex()

    init {
        repeat(maxPlayers) { pool.add(createPlayer(app)) }
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

            // vecinii preload: playWhenReady false
            player.playWhenReady = (idx == focusedIndex)
        }

        // 3) după ensure, setează focus corect
        applyFocus(centerIndex)
    }

    fun onPageSettled(index: Int, post: Post?) {
        focusedIndex = index
        _currentPost.value = post
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
        // aici pui url-ul tău (hls/mp4)
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
        player.volume = 0f
    }

    fun getPlayerForIndex(index: Int): ExoPlayer? = indexToPlayer[index]

    fun stopDetailSession() {
        indexToPlayer.values.forEach { player ->
            player.playWhenReady = false
            player.pause()
            player.stop()
            player.clearMediaItems()
            player.volume = 0f
        }
        indexToPlayer.clear()
        indexToPostId.clear()

        // IMPORTANT: NU clear pool, NU release players
        focusedIndex = null
    }

    override fun onCleared() {
        stopDetailSession()
        super.onCleared()
    }

    // Edit
    private val _editState = MutableStateFlow<FeatureState<Unit>?>(null)
    val editState: StateFlow<FeatureState<Unit>?> = _editState

    private val _photoUri = MutableStateFlow<Uri?>(null)
    val photoUri: StateFlow<Uri?> = _photoUri.asStateFlow()

    fun setPhoto(uri: Uri) {
        _photoUri.value = uri
    }

    var isSaved by mutableStateOf(false)

    fun updateFullName(newFullName: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateFullNameUseCase(newFullName) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(fullName = newFullName)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit FullName User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = updateUsernameUseCase(newUsername)

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(username = newUsername)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Username User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateBio(newBio: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateBioUseCase(newBio) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data

                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(bio = newBio)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Bio User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateGender(newGender: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateGenderUseCase(newGender) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(gender = newGender)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Gender User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updateWebsite(newWebsite: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updateWebsiteUseCase(newWebsite) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(website = newWebsite)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Website User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }

    fun updatePublicEmail(newPublicEmail: String) {
        viewModelScope.launch {
            _editState.value = FeatureState.Loading

            val result = withVisibleLoading { updatePublicEmailUseCase(newPublicEmail) }

            result
                .onSuccess {
                    _editState.value = FeatureState.Success(Unit)

                    val currentProfile = (_userProfileState.value as? FeatureState.Success)?.data
                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(publicEmail = newPublicEmail)
                        _userProfileState.value = FeatureState.Success(updatedProfile)
                    }
                    isSaved = true
                }
                .onFailure { error ->
                    Timber.Forest.tag("EditProfile").e(error, "ERROR: on Edit Public Email User Data")
                    _editState.value = FeatureState.Error(error = null)
                }
        }
    }
}
package com.example.scrollbooker.ui.profile

import android.content.Context
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.SavedStateHandle
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
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.customized.post.VideoPlayerCache
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileAboutUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.plus
import kotlin.collections.set

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    private val getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unBookmarkPostUseCase: UnBookmarkPostUseCase,
    @ApplicationContext private val app: Context,
    private val videoPlayerManager: VideoPlayerManager,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val userIdFlow: StateFlow<Int?> = savedStateHandle.getStateFlow("userId", null)
    private val username: StateFlow<String?> = savedStateHandle.getStateFlow("username", null)

    val userId: Int
        get() = userIdFlow.value ?: error("UserId is required and cannot be null")

    private val _currentTab = MutableStateFlow<Int>(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _isFollowState = MutableStateFlow<Boolean?>(null)
    val isFollowState: StateFlow<Boolean?> = _isFollowState.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val profileMutations = MutableSharedFlow<FeatureState<UserProfile>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val profile: StateFlow<FeatureState<UserProfile>> = merge(
        username
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { currentUsername ->
                flow {
                    emit(FeatureState.Loading)
                    val lat = 44.446145f
                    val lng = 25.989074f

                    val response = withVisibleLoading {
                        getUserProfileUseCase(currentUsername, lat, lng)
                    }

                    if (response is FeatureState.Success) {
                        _isFollowState.value = response.data.isFollow
                    }
                    emit(response)
                }
            },
        profileMutations
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = FeatureState.Loading
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: Flow<PagingData<Post>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId -> getUserPostsUseCase(currentUserId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees: Flow<PagingData<Employee>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId -> getEmployeesByOwnerUseCase(currentUserId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookmarks: Flow<PagingData<Post>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId -> getUserBookmarkedPostsUseCase(currentUserId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products: StateFlow<FeatureState<UserProducts>> = profile
        .mapNotNull { state -> if (state is FeatureState.Success) state.data else null }
        .mapNotNull { userProfile ->
            val businessId = userProfile.businessId ?: return@mapNotNull null
            val isEmployee = userProfile.businessOwner?.id != userProfile.id
            val employeeId = if (isEmployee) userProfile.id else null

            Pair(businessId, employeeId)
        }
        .distinctUntilChanged()
        .flatMapLatest { (businessId, employeeId) ->
            flow {
                emit(FeatureState.Loading)
                val result = withVisibleLoading {
                    getProductsByBusinessIdAndEmployeeIdUseCase(
                        businessId = businessId,
                        employeeId = employeeId,
                        onlyServicesWithProducts = true,
                        productsLimitPerService = null
                    )
                }
                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val about: StateFlow<FeatureState<UserProfileAbout>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId ->
            flow {
                emit(FeatureState.Loading)
                emit(withVisibleLoading { getUserProfileAboutUseCase(currentUserId) })
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val schedules: StateFlow<FeatureState<List<Schedule>>> = userIdFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { currentUserId ->
            flow {
                emit(FeatureState.Loading)
                emit(withVisibleLoading { getSchedulesByUserIdUseCase(currentUserId) })
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    fun follow() {
        viewModelScope.launch {
            val targetUserId = userId

            if (_isSaving.value) return@launch

            val previous = isFollowState.value == true
            val optimistic = !previous

            val currentProfile = (profile.value as? FeatureState.Success)?.data
            val oldFollowersCount = currentProfile?.counters?.followersCount ?: 0

            _isSaving.value = true
            _isFollowState.value = optimistic

            fun updateProfileState(countOffset: Int) {
                if (currentProfile != null) {
                    val updated = currentProfile.copy(
                        counters = currentProfile.counters.copy(
                            followersCount = oldFollowersCount + countOffset
                        )
                    )
                    viewModelScope.launch { profileMutations.emit(FeatureState.Success(updated)) }
                }
            }

            try {
                if (optimistic) {
                    followUserUseCase(targetUserId)
                    updateProfileState(1)
                } else {
                    unfollowUserUseCase(targetUserId)
                    updateProfileState(-1)
                }
            } catch (e: Exception) {
                Timber.tag("Follow/Unfollow").e(e, "ERROR: on Follow/Unfollow Action")
                _isFollowState.value = previous
                updateProfileState(0)
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun setCurrentTab(index: Int) {
        _currentTab.value = index
    }

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

    val userPausedPostIds = videoPlayerManager.userPausedPostIds

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    fun getPlayerForIndex(scopeKey: String, index: Int): ExoPlayer? {
        return videoPlayerManager.getPlayerForIndex(scopeKey, index)
    }

    fun setDetailScreenActive(isActive: Boolean, scopeKey: String, centerIndex: Int, getPost: (Int) -> Post?) {
        if (isActive) {
            videoPlayerManager.activateScreenScope(scopeKey)
        }
        videoPlayerManager.ensureWindow(scopeKey, centerIndex, isActive, getPost)
    }

    fun onPostSettled(scopeKey: String, index: Int, getPost: (Int) -> Post?) {
        _currentIndex.value = index
        videoPlayerManager.onPageSettled(scopeKey, index, true)
        videoPlayerManager.ensureWindow(scopeKey, index, true, getPost)
    }

    fun togglePlayPause(scopeKey: String, index: Int) {
        videoPlayerManager.togglePlayer(scopeKey, index)
    }

    fun onDetailSessionFinished(scopeKey: String) {
        videoPlayerManager.releaseScreenScope(scopeKey)
    }

    override fun onCleared() {
        super.onCleared()
        // Curățăm preventiv orice sesiune de detalii deschisă pentru acest utilizator specific
        videoPlayerManager.releaseScreenScope("USER_PROFILE_DETAIL_POSTS_${userId}")
        videoPlayerManager.releaseScreenScope("USER_PROFILE_DETAIL_BOOKMARKS_${userId}")
    }
}
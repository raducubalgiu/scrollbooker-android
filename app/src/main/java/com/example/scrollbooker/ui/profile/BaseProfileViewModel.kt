package com.example.scrollbooker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.util.FeatureState
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

abstract class BaseProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unBookmarkPostUseCase: UnBookmarkPostUseCase,
    protected val videoPlayerManager: VideoPlayerManager
) : ViewModel(), ProfilePostDetailViewModelContract {
    abstract val userIdFlow: Flow<Int?>
    abstract val usernameFlow: Flow<String?>

    protected val _isFollowState = MutableStateFlow<Boolean?>(null)
    val isFollowState: StateFlow<Boolean?> = _isFollowState.asStateFlow()

    protected val profileMutations = MutableSharedFlow<FeatureState<UserProfile>>()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val profile: StateFlow<FeatureState<UserProfile>> by lazy {
        merge(
            usernameFlow
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { currentUsername ->
                    flow {
                        emit(FeatureState.Loading)
                        val response = getUserProfileUseCase(currentUsername, lat = null, lng = null)

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
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val posts: Flow<PagingData<Post>> by lazy {
        userIdFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { currentUserId -> getUserPostsUseCase(currentUserId) }
            .cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val employees: Flow<PagingData<Employee>> by lazy {
        userIdFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { currentUserId -> getEmployeesByOwnerUseCase(currentUserId) }
            .cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val bookmarks: Flow<PagingData<Post>> by lazy {
        userIdFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { currentUserId -> getUserBookmarkedPostsUseCase(currentUserId) }
            .cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val products: StateFlow<FeatureState<UserProducts>> by lazy {
        profile
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
                    val result = getProductsByBusinessIdAndEmployeeIdUseCase(
                        businessId = businessId,
                        employeeId = employeeId,
                        onlyServicesWithProducts = true,
                        productsLimitPerService = null
                    )
                    emit(result)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Lazily,
                FeatureState.Loading
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val about: StateFlow<FeatureState<UserProfileAbout>> by lazy {
        userIdFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { currentUserId ->
                flow {
                    emit(FeatureState.Loading)
                    emit(getUserProfileAboutUseCase(currentUserId))
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, FeatureState.Loading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val schedules: StateFlow<FeatureState<List<Schedule>>> by lazy {
        userIdFlow
            .filterNotNull()
            .distinctUntilChanged()
            .flatMapLatest { currentUserId ->
                flow {
                    emit(FeatureState.Loading)
                    emit(getSchedulesByUserIdUseCase(currentUserId))
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, FeatureState.Loading)
    }

    // Post Interactions
    protected val _postUi = MutableStateFlow<Map<Int, PostActionUiState>>(emptyMap())

    override fun observePostUi(postId: Int): StateFlow<PostActionUiState> =
        _postUi.map { it[postId] ?: PostActionUiState.EMPTY }
            .stateIn(viewModelScope, SharingStarted.Eagerly, PostActionUiState.EMPTY)

    protected inline fun MutableStateFlow<Map<Int, PostActionUiState>>.edit(
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

    override fun toggleLike(post: Post) {
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

    override fun toggleBookmark(post: Post) {
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
    override fun getPlayerForIndex(scopeKey: String, index: Int): ExoPlayer? {
        return videoPlayerManager.getPlayerForIndex(scopeKey, index)
    }

    override fun setDetailScreenActive(isActive: Boolean, scopeKey: String, centerIndex: Int, getPost: (Int) -> Post?) {
        if (isActive) {
            videoPlayerManager.activateScreenScope(scopeKey)
        }
        videoPlayerManager.ensureWindow(scopeKey, centerIndex, isActive, getPost)
    }

    override fun onPostSettled(scopeKey: String, index: Int, getPost: (Int) -> Post?) {
        videoPlayerManager.onPageSettled(scopeKey, index, true)
        videoPlayerManager.ensureWindow(scopeKey, index, true, getPost)
    }

    override fun togglePlayPause(scopeKey: String, index: Int) {
        videoPlayerManager.togglePlayer(scopeKey, index)
    }

    override fun onDetailSessionFinished(scopeKey: String) {
        videoPlayerManager.releaseScreenScope(scopeKey)
    }
}
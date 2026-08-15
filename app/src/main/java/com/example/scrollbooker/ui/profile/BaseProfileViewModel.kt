package com.example.scrollbooker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.components.customized.post.PostActionUiState
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.components.customized.post.PostViewHeartbeatTracker
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
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
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn

abstract class BaseProfileViewModel(
    private val shouldShowVisibleLoading: Boolean,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    private val getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    private val postInteractionStore: PostInteractionStore,
    protected val videoPlayerManager: VideoPlayerManager,
    @Suppress("UNUSED_PARAMETER") postViewHeartbeatTracker: PostViewHeartbeatTracker
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

                        val response = if (shouldShowVisibleLoading) {
                            withVisibleLoading {
                                getUserProfileUseCase(currentUsername, lat = null, lng = null)
                            }
                        } else {
                            getUserProfileUseCase(currentUsername, lat = null, lng = null)
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

    override fun observePostUi(postId: Int): StateFlow<PostActionUiState> =
        postInteractionStore.observePostUi(postId)

    override fun toggleLike(post: Post) {
        postInteractionStore.toggleLike(post)
    }

    override fun toggleBookmark(post: Post) {
        postInteractionStore.toggleBookmark(post)
    }

    override fun sharePost(post: Post, channel: ShareChannelEnum) {
        postInteractionStore.sharePost(post, channel)
    }

    override val userPausedPostIds: StateFlow<Set<Int>> = videoPlayerManager.userPausedPostIds

    override fun getPlayerForIndex(scopeKey: String, index: Int): ExoPlayer? {
        return videoPlayerManager.getPlayerForIndex(scopeKey, index)
    }

    override fun setDetailScreenActive(
        isActive: Boolean,
        scopeKey: String,
        centerIndex: Int,
        getPost: (Int) -> Post?) {
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
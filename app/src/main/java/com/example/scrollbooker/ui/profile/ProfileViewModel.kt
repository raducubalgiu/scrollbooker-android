package com.example.scrollbooker.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileAboutUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    private val getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    private val getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    private val getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val userId: StateFlow<Int?> = savedStateHandle.getStateFlow("userId", null)

    private val _currentTab = MutableStateFlow<Int>(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _profile = MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val profile: StateFlow<FeatureState<UserProfile>> = _profile.asStateFlow()

    private val _isFollowState = MutableStateFlow<Boolean?>(null)
    val isFollowState: StateFlow<Boolean?> = _isFollowState.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        userId.filterNotNull()
            .onEach { id -> loadUserProfile(id) }
            .launchIn(viewModelScope)
    }

    fun loadUserProfile(userId: Int) {
        viewModelScope.launch {
            _profile.value = FeatureState.Loading

            val lat = 44.446145f
            val lng = 25.989074f

            val response = withVisibleLoading {
                getUserProfileUseCase(userId, lat, lng)
            }

            if(response is FeatureState.Success) {
                val profile = response.data
                _isFollowState.value = profile.isFollow
            }

            _profile.value = response
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val posts: Flow<PagingData<Post>> = userId
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val employees: Flow<PagingData<Employee>> = userId
        .filterNotNull()
        .flatMapLatest { userId -> getEmployeesByOwnerUseCase(userId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val bookmarks: Flow<PagingData<Post>> = userId
        .filterNotNull()
        .flatMapLatest { userId -> getUserBookmarkedPostsUseCase(userId) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val about: StateFlow<FeatureState<UserProfileAbout>> = userId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)
                emit(withVisibleLoading { getUserProfileAboutUseCase(userId) })
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val schedules: StateFlow<FeatureState<List<Schedule>>> = userId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { userId ->
            flow {
                emit(FeatureState.Loading)

                val result = withVisibleLoading {
                    getSchedulesByUserIdUseCase(userId)
                }

                emit(result)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = FeatureState.Loading
        )

    fun follow() {
        viewModelScope.launch {
            val targetUserId = userId.value

            if(targetUserId == null) return@launch
            if(_isSaving.value) return@launch

            val previous = isFollowState.value == true
            val optimistic = !previous

            val currentProfile = (_profile.value as? FeatureState.Success)?.data
            val oldFollowersCount = currentProfile?.counters?.followersCount ?: 0

            _isSaving.value = true
            _isFollowState.value = optimistic

            try {
                if(optimistic) {
                    followUserUseCase(targetUserId)

                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(
                            counters = currentProfile.counters.copy(
                                followersCount = oldFollowersCount + 1
                            )
                        )
                        _profile.value = FeatureState.Success(updatedProfile)
                    }
                } else {
                    unfollowUserUseCase(targetUserId)

                    if(currentProfile != null) {
                        val updatedProfile = currentProfile.copy(
                            counters = currentProfile.counters.copy(
                                followersCount = oldFollowersCount - 1
                            )
                        )
                        _profile.value = FeatureState.Success(updatedProfile)
                    }
                }
            } catch(e: Exception) {
                Timber.tag("Follow/Unfollow").e(e, "ERROR: on Follow/Unfollow Action")
                _isFollowState.value = previous

                if(currentProfile != null) {
                    val updatedProfile = currentProfile.copy(
                        counters = currentProfile.counters.copy(
                            followersCount = oldFollowersCount
                        )
                    )
                    _profile.value = FeatureState.Success(updatedProfile)
                }
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun setCurrentTab(index: Int) {
        _currentTab.value = index
    }
}
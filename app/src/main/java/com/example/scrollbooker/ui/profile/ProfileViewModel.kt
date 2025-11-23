package com.example.scrollbooker.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val userId: StateFlow<Int?> = savedStateHandle.getStateFlow("userId", null)

    private val _userProfileState =
        MutableStateFlow<FeatureState<UserProfile>>(FeatureState.Loading)
    val userProfileState: StateFlow<FeatureState<UserProfile>> = _userProfileState

    private val _initCompleted = MutableStateFlow(false)
    val isInitLoading = combine(_userProfileState, _initCompleted) { profile, done ->
        (profile is FeatureState.Loading || !done)
    }.stateIn(viewModelScope, SharingStarted.Companion.Eagerly, true)

    private val _isFollowState = MutableStateFlow<Boolean?>(null)
    val isFollowState: StateFlow<Boolean?> = _isFollowState.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val userPosts: StateFlow<PagingData<Post>> = userId
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .onEach { _initCompleted.value = true }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Companion.Lazily, PagingData.Companion.empty())

    init {
        userId.filterNotNull()
            .onEach { id -> loadUserProfile(id) }
            .launchIn(viewModelScope)
    }

    fun loadUserProfile(userId: Int) {
        viewModelScope.launch {
            _userProfileState.value = FeatureState.Loading

            val lat = 44.446145f
            val lng = 25.989074f

            val response = withVisibleLoading {
                getUserProfileUseCase(userId, lat, lng)
            }

            if(response is FeatureState.Success) {
                val profile = response.data
                _isFollowState.value = profile.isFollow
            }

            _userProfileState.value = response
        }
    }

    fun onFollow() {
        viewModelScope.launch {
            val targetUserId = userId.value

            if(targetUserId == null) return@launch
            if(_isSaving.value) return@launch

            val previous = isFollowState.value == true
            val optimistic = !previous

            _isSaving.value = true
            _isFollowState.value = optimistic

            try {
                if(optimistic) {
                    followUserUseCase(targetUserId)
                } else {
                    unfollowUserUseCase(targetUserId)
                }
            } catch(e: Exception) {
                Timber.tag("Follow/Unfollow").e("ERROR: $e")
                _isFollowState.value = previous
            } finally {
                _isSaving.value = false
            }
        }
    }
}
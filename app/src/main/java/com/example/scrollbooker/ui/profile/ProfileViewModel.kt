package com.example.scrollbooker.ui.profile
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.components.customized.post.VideoPlayerManager
import com.example.scrollbooker.entity.booking.employee.domain.useCase.GetEmployeesByOwnerUseCase
import com.example.scrollbooker.entity.booking.products.domain.useCase.GetProductsByBusinessIdAndEmployeeIdUseCase
import com.example.scrollbooker.entity.booking.schedule.domain.useCase.GetSchedulesByUserIdUseCase
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.social.post.domain.useCase.UnLikePostUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileAboutUseCase
import com.example.scrollbooker.entity.user.userProfile.domain.usecase.GetUserProfileUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    savedStateHandle: SavedStateHandle,

    getUserProfileUseCase: GetUserProfileUseCase,
    getUserPostsUseCase: GetUserPostsUseCase,
    getEmployeesByOwnerUseCase: GetEmployeesByOwnerUseCase,
    getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase,
    getProductsByBusinessIdAndEmployeeIdUseCase: GetProductsByBusinessIdAndEmployeeIdUseCase,
    getSchedulesByUserIdUseCase: GetSchedulesByUserIdUseCase,
    getUserProfileAboutUseCase: GetUserProfileAboutUseCase,
    likePostUseCase: LikePostUseCase,
    unLikePostUseCase: UnLikePostUseCase,
    bookmarkPostUseCase: BookmarkPostUseCase,
    unBookmarkPostUseCase: UnBookmarkPostUseCase,
    videoPlayerManager: VideoPlayerManager
): BaseProfileViewModel(
    getUserProfileUseCase = getUserProfileUseCase,
    getUserPostsUseCase = getUserPostsUseCase,
    getEmployeesByOwnerUseCase = getEmployeesByOwnerUseCase,
    getUserBookmarkedPostsUseCase = getUserBookmarkedPostsUseCase,
    getProductsByBusinessIdAndEmployeeIdUseCase = getProductsByBusinessIdAndEmployeeIdUseCase,
    getSchedulesByUserIdUseCase = getSchedulesByUserIdUseCase,
    getUserProfileAboutUseCase = getUserProfileAboutUseCase,
    likePostUseCase = likePostUseCase,
    unLikePostUseCase = unLikePostUseCase,
    bookmarkPostUseCase = bookmarkPostUseCase,
    unBookmarkPostUseCase = unBookmarkPostUseCase,
    videoPlayerManager = videoPlayerManager
) {
    private val userIdFlowInternal: StateFlow<Int?> = savedStateHandle.getStateFlow("userId", null)
    private val usernameInternal: StateFlow<String?> = savedStateHandle.getStateFlow("username", null)

    val userId: Int
        get() = userIdFlowInternal.value ?: error("UserId is required and cannot be null")

    override val userIdFlow: Flow<Int?> = userIdFlowInternal
    override val usernameFlow: Flow<String?> = usernameInternal

    private val _currentTab = MutableStateFlow<Int>(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

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

    override fun onCleared() {
        super.onCleared()
        videoPlayerManager.releaseScreenScope("USER_PROFILE_DETAIL_POSTS_${userId}")
        videoPlayerManager.releaseScreenScope("USER_PROFILE_DETAIL_BOOKMARKS_${userId}")
    }
}
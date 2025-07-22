package com.example.scrollbooker.ui.profile.social

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.review.domain.model.Review
import com.example.scrollbooker.entity.booking.review.domain.model.ReviewsSummary
import com.example.scrollbooker.entity.booking.review.domain.useCase.GetReviewsSummaryUseCase
import com.example.scrollbooker.entity.booking.review.domain.useCase.GetReviewsUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.GetUserSocialFollowersUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.GetUserSocialFollowingsUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserSocialViewModel @Inject constructor(
    private val getUserSocialFollowingsUseCase: GetUserSocialFollowingsUseCase,
    private val getUserSocialFollowersUseCase: GetUserSocialFollowersUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReviewsSummaryUseCase: GetReviewsSummaryUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val userId: Int = savedStateHandle["userId"] ?: error("Missing userId")

    private val filtersTrigger = MutableStateFlow(Unit)

    private val _selectedRatings = mutableStateOf(setOf<Int>())
    val selectedRatings: State<Set<Int>> get() = _selectedRatings

    @OptIn(ExperimentalCoroutinesApi::class)
    val userReviews: Flow<PagingData<Review>> =
        combine(filtersTrigger, snapshotFlow { _selectedRatings.value }) { _, ratings ->
            ratings
        }.flatMapLatest { ratings ->
            getReviewsUseCase(userId, ratings.ifEmpty { null })
        }.cachedIn(viewModelScope)

    private val _userReviewsSummary = MutableStateFlow<FeatureState<ReviewsSummary>>(FeatureState.Loading)
    val userReviewsSummary: StateFlow<FeatureState<ReviewsSummary>> = _userReviewsSummary

    private val _userFollowers: Flow<PagingData<UserSocial>> by lazy {
        getUserSocialFollowersUseCase(userId).cachedIn(viewModelScope)
    }
    val userFollowers: Flow<PagingData<UserSocial>> get() = _userFollowers

    private val _userFollowings: Flow<PagingData<UserSocial>> by lazy {
        getUserSocialFollowingsUseCase(userId).cachedIn(viewModelScope)
    }
    val userFollowings: Flow<PagingData<UserSocial>> get() = _userFollowings

    private val _followedOverrides = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val followedOverrides = _followedOverrides.asStateFlow()

    private val _followRequestLocks = MutableStateFlow<Set<Int>>(emptySet())
    val followRequestLocks = _followRequestLocks.asStateFlow()

    fun loadUserReviews() {
        viewModelScope.launch {
            _userReviewsSummary.value = FeatureState.Loading
            _userReviewsSummary.value = getReviewsSummaryUseCase(userId)
        }
    }

    fun onFollow(isFollow: Boolean, userId: Int) {
        if(_followRequestLocks.value.contains(userId)) {
            return
        }

        _followRequestLocks.update { it + userId }
        _followedOverrides.update { it + (userId to !isFollow) }

        viewModelScope.launch {
            try {
                if(isFollow) {
                    unfollowUserUseCase(userId)
                } else {
                    followUserUseCase(userId)
                }
            } catch(e: Exception) {
                _followedOverrides.update { it + (userId to isFollow) }
                Timber.tag("Follow/Unfollow").e("ERROR: $e")

            } finally {
                _followRequestLocks.update { it - userId }
            }
        }
    }

    fun toggleRatingFilter(rating: Int) {
        _selectedRatings.value =
            if(rating in _selectedRatings.value) {
                _selectedRatings.value - rating
            } else {
                _selectedRatings.value + rating
            }

        filtersTrigger.value = Unit
    }
}
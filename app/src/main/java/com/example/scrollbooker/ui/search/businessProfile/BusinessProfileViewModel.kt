package com.example.scrollbooker.ui.search.businessProfile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.withVisibleLoading
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.useCase.GetBusinessProfileUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import com.example.scrollbooker.navigation.routes.MainRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class BusinessProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getBusinessProfileUseCase: GetBusinessProfileUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
): ViewModel() {
    private val businessId: Int = checkNotNull(savedStateHandle[MainRoute.BusinessProfile.ARG_BUSINESS_ID])

    private val _isFollowState = MutableStateFlow<Boolean?>(null)
    val isFollowState: StateFlow<Boolean?> = _isFollowState.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _businessProfileState = MutableStateFlow<FeatureState<BusinessProfile>>(FeatureState.Loading)
    val businessProfileState: StateFlow<FeatureState<BusinessProfile>> = _businessProfileState.asStateFlow()

    private fun loadUserProfile() {
        viewModelScope.launch {
            _businessProfileState.value = FeatureState.Loading

            val response = withVisibleLoading { getBusinessProfileUseCase(businessId) }

            if(response is FeatureState.Success) {
                val businessProfile = response.data
                _isFollowState.value = businessProfile.owner.isFollow
            }

            _businessProfileState.value = response
        }
    }

    init {
        loadUserProfile()
    }

    fun follow() {
        viewModelScope.launch {
            if (_isSaving.value) return@launch

            val previousFollow = _isFollowState.value == true
            val optimisticFollow = !previousFollow

            val previousState = _businessProfileState.value
            val previousProfile = (previousState as? FeatureState.Success)?.data
                ?: return@launch

            val previousFollowersCount = previousProfile.owner.counters.followersCount
            val newFollowersCount = (previousFollowersCount + if (optimisticFollow) 1 else -1)
                .coerceAtLeast(0)

            _isSaving.value = true
            _isFollowState.value = optimisticFollow

            _businessProfileState.value = FeatureState.Success(
                previousProfile.copy(
                    owner = previousProfile.owner.copy(
                        counters = previousProfile.owner.counters.copy(
                            followersCount = newFollowersCount
                        )
                    )
                )
            )

            try {
                if (optimisticFollow) {
                    followUserUseCase(previousProfile.owner.id)
                } else {
                    unfollowUserUseCase(previousProfile.owner.id)
                }
            } catch (e: Exception) {
                Timber.tag("Follow/Unfollow").e(e, "ERROR: on Follow/Unfollow Action")

                _isFollowState.value = previousFollow
                _businessProfileState.value = previousState
            } finally {
                _isSaving.value = false
            }
        }
    }
}
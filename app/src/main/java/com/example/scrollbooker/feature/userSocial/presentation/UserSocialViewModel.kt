package com.example.scrollbooker.feature.userSocial.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial
import com.example.scrollbooker.feature.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.feature.userSocial.domain.useCase.GetUserSocialFollowersUseCase
import com.example.scrollbooker.feature.userSocial.domain.useCase.GetUserSocialFollowingsUseCase
import com.example.scrollbooker.feature.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UserSocialViewModel @Inject constructor(
    private val getUserSocialFollowingsUseCase: GetUserSocialFollowingsUseCase,
    private val getUserSocialFollowersUseCase: GetUserSocialFollowersUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val userId: Int = savedStateHandle["userId"] ?: error("Missing userId")

    private val _userFollowers: Flow<PagingData<UserSocial>> by lazy {
        getUserSocialFollowersUseCase(userId).cachedIn(viewModelScope)
    }

    private val _userFollowings: Flow<PagingData<UserSocial>> by lazy {
        getUserSocialFollowingsUseCase(userId).cachedIn(viewModelScope)
    }

    val userFollowers: Flow<PagingData<UserSocial>> get() = _userFollowers
    val userFollowings: Flow<PagingData<UserSocial>> get() = _userFollowings

    private val _followedOverrides = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val followedOverrides = _followedOverrides.asStateFlow()

    private val _followRequestLocks = MutableStateFlow<Set<Int>>(emptySet())
    val followRequestLocks = _followRequestLocks.asStateFlow()

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

    fun resetOverrides() {
        _followedOverrides.value = emptyMap()
    }
}
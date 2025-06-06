package com.example.scrollbooker.feature.userSocial.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial
import com.example.scrollbooker.feature.userSocial.domain.useCase.GetUserSocialFollowersUseCase
import com.example.scrollbooker.feature.userSocial.domain.useCase.GetUserSocialFollowingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserSocialViewModel @Inject constructor(
    private val getUserSocialFollowingsUseCase: GetUserSocialFollowingsUseCase,
    private val getUserSocialFollowersUseCase: GetUserSocialFollowersUseCase,
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

}
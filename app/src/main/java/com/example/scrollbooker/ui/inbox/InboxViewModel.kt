package com.example.scrollbooker.ui.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.entity.user.notification.domain.useCase.GetNotificationsUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class InboxViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
): ViewModel() {
    private val _notificationRefreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val notifications: Flow<PagingData<Notification>> = _notificationRefreshTrigger
        .flatMapLatest { getNotificationsUseCase() }
        .cachedIn(viewModelScope)

    private val _followState = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val followState = _followState.asStateFlow()

    private val _isFollowSaving = MutableStateFlow<Set<Int>>(emptySet())
    val isFollowSaving = _isFollowSaving.asStateFlow()

    fun refreshNotifications() {
        _notificationRefreshTrigger.value += 1
    }

    fun follow(isFollow: Boolean, userId: Int) {
        if(_isFollowSaving.value.contains(userId)) {
            return
        }

        _isFollowSaving.update { it + userId }
        _followState.update { it + (userId to !isFollow) }

        viewModelScope.launch {
            try {
                if(isFollow) {
                    unfollowUserUseCase(userId)
                } else {
                    followUserUseCase(userId)
                }
            } catch(e: Exception) {
                _followState.update { it + (userId to isFollow) }
                Timber.tag("Follow/Unfollow").e("ERROR: $e")

            } finally {
                _isFollowSaving.update { it - userId }
            }
        }
    }
}
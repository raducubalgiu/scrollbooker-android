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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val _notifications: Flow<PagingData<Notification>> by lazy {
        getNotificationsUseCase().cachedIn(viewModelScope)
    }

    val notifications:  Flow<PagingData<Notification>> get() = _notifications

    private val _followedOverrides = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val followedOverrides = _followedOverrides.asStateFlow()

    private val _followRequestLocks = MutableStateFlow<Set<Int>>(emptySet())
    val followRequestLocks = _followRequestLocks.asStateFlow()

    fun follow(isFollow: Boolean, userId: Int) {
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
}
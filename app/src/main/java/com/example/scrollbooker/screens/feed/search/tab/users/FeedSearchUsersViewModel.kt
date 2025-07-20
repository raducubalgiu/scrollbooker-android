package com.example.scrollbooker.screens.feed.search.tab.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.search.domain.useCase.SearchPaginatedUsersUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.FollowUserUseCase
import com.example.scrollbooker.entity.user.userSocial.domain.useCase.UnfollowUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject
import kotlin.collections.plus

@HiltViewModel
class FeedSearchUsersViewModel @Inject constructor(
    private val searchPaginatedUsersUseCase: SearchPaginatedUsersUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unfollowUserUseCase: UnfollowUserUseCase,
): ViewModel() {
    private val currentQuery = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val usersState: Flow<PagingData<UserSocial>> = currentQuery
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { query ->
            searchPaginatedUsersUseCase(query)
        }
        .cachedIn(viewModelScope)

    fun setQuery(query: String) {
        currentQuery.value = query
    }

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
}
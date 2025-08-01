package com.example.scrollbooker.ui.profile.tab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.social.bookmark.domain.useCase.GetUserBookmarkedPostsUseCase
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.useCase.GetUserPostsUseCase
import com.example.scrollbooker.entity.social.repost.domain.useCase.GetUserRepostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileTabViewModel @Inject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserRepostsUseCase: GetUserRepostsUseCase,
    private val getUserBookmarkedPostsUseCase: GetUserBookmarkedPostsUseCase
): ViewModel() {
    private val userIdState = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val userPosts: StateFlow<PagingData<Post>> = userIdState
        .filterNotNull()
        .flatMapLatest { userId -> getUserPostsUseCase(userId) }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    @OptIn(ExperimentalCoroutinesApi::class)
    val userReposts: StateFlow<PagingData<Post>> = userIdState
        .filterNotNull()
        .flatMapLatest { userId ->
            getUserRepostsUseCase(userId)
        }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = PagingData.empty()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val userBookmarkedPosts: StateFlow<PagingData<Post>> = userIdState
        .filterNotNull()
        .flatMapLatest { userId ->
            getUserBookmarkedPostsUseCase(userId)
        }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    fun setUserId(userId: Int) {
        if(userIdState.value != userId) {
            userIdState.value = userId
        }
    }
}
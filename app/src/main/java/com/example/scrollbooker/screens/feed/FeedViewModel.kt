package com.example.scrollbooker.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.entity.post.domain.useCase.GetBookNowPostsUseCase
import com.example.scrollbooker.entity.post.domain.useCase.GetFollowingPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getBookNowPostsUseCase: GetBookNowPostsUseCase,
    getFollowingPostsUseCase: GetFollowingPostsUseCase
): ViewModel() {

    private val _selectedBusinessTypes = MutableStateFlow<Set<Int>>(emptySet())
    val selectedBusinessTypes: StateFlow<Set<Int>> = _selectedBusinessTypes

    private val _bookNowPosts: Flow<PagingData<Post>> by lazy {
        getBookNowPostsUseCase()
            .cachedIn(viewModelScope)
    }
    val bookNowPosts: Flow<PagingData<Post>> get() = _bookNowPosts

    private val _followingPosts: Flow<PagingData<Post>> by lazy {
        getFollowingPostsUseCase()
            .cachedIn(viewModelScope)
    }
    val followingPosts: Flow<PagingData<Post>> get() = _followingPosts

    fun setBusinessType(id: Int) {
        _selectedBusinessTypes.update { current ->
            if(current.contains(id)) current - id else current + id
        }
    }

    fun clearBusinessTypes() {
        _selectedBusinessTypes.value = emptySet()
    }

    fun filterBookNowPosts() {

    }
}
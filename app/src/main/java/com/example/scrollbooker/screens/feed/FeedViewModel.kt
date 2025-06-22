package com.example.scrollbooker.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.shared.post.domain.model.Post
import com.example.scrollbooker.shared.post.domain.useCase.GetBookNowPostsUseCase
import com.example.scrollbooker.shared.post.domain.useCase.GetFollowingPostsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getBookNowPostsUseCase: GetBookNowPostsUseCase,
    getFollowingPostsUseCase: GetFollowingPostsUseCase
): ViewModel() {

    private val _bookNowPosts: Flow<PagingData<Post>> by lazy {
        getBookNowPostsUseCase().cachedIn(viewModelScope)
    }
    val bookNowPosts: Flow<PagingData<Post>> get() = _bookNowPosts

    private val _followingPosts: Flow<PagingData<Post>> by lazy {
        getFollowingPostsUseCase().cachedIn(viewModelScope)
    }
    val followingPosts: Flow<PagingData<Post>> get() = _followingPosts

    init {
        Timber.tag("Init").e("-> Feed - View Model Created")
    }
}
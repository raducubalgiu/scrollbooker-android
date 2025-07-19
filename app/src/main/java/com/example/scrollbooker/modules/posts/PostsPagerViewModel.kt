package com.example.scrollbooker.modules.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.entity.post.domain.useCase.BookmarkPostUseCase
import com.example.scrollbooker.entity.post.domain.useCase.LikePostUseCase
import com.example.scrollbooker.entity.post.domain.useCase.UnBookmarkPostUseCase
import com.example.scrollbooker.entity.post.domain.useCase.UnLikePostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsPagerViewModel @Inject constructor(
    private val likePostUseCase: LikePostUseCase,
    private val unLikePostUseCase: UnLikePostUseCase,
    private val bookmarkPostUseCase: BookmarkPostUseCase,
    private val unBookmarkPostUseCase: UnBookmarkPostUseCase
): ViewModel() {
    private val _interactionMap = mutableMapOf<Int, MutableStateFlow<PostInteractionState>>()

    fun interactionState(postId: Int): StateFlow<PostInteractionState> {
        return _interactionMap.getOrPut(postId) {
            MutableStateFlow(PostInteractionState())
        }
    }

    fun setInitialState(
        postId: Int,
        isLiked: Boolean,
        likeCount: Int,
        isBookmarked: Boolean,
        bookmarkCount: Int
    ) {
        val flow = _interactionMap.getOrPut(postId) {
            MutableStateFlow(PostInteractionState())
        }

        if(flow.value == PostInteractionState()) {
            flow.value = PostInteractionState(
                isLiked = isLiked,
                likeCount = likeCount,
                isBookmarked = isBookmarked,
                bookmarkCount = bookmarkCount
            )
        }
    }

    fun toggleLike(postId: Int) {
        val flow = _interactionMap[postId] ?: return
        val current = flow.value

        if(current.isLiking) return

        val newState = current.copy(
            isLiked = !current.isLiked,
            likeCount = current.likeCount + if(!current.isLiked) 1 else -1,
            isLiking = true
        )

        flow.value = newState

        viewModelScope.launch {
            val result = if(!current.isLiked) {
                likePostUseCase(postId)
            } else {
                unLikePostUseCase(postId)
            }

            flow.value = if(result.isFailure) current else newState.copy(isLiking = false)
        }
    }

    fun toggleBookmark(postId: Int) {
        val flow = _interactionMap[postId] ?: return
        val current = flow.value

        if(current.isBookmarking) return

        val newState = current.copy(
            isBookmarked = !current.isBookmarked,
            bookmarkCount = current.bookmarkCount + if(!current.isBookmarked) 1 else -1,
            isBookmarking = true
        )

        flow.value = newState

        viewModelScope.launch {
            val result = if(!current.isBookmarked) {
                bookmarkPostUseCase(postId)
            } else {
                unBookmarkPostUseCase(postId)
            }

            flow.value = if(result.isFailure) current else newState.copy(isBookmarking = false)
        }
    }
}
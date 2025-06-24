package com.example.scrollbooker.components.customized.post.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.shared.comment.domain.model.Comment
import com.example.scrollbooker.shared.comment.domain.useCase.GetPaginatedPostCommentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPaginatedPostCommentsUseCase
): ViewModel() {
    private val _commentsState = MutableStateFlow<PagingData<Comment>>(PagingData.empty())
    val commentsPosts: StateFlow<PagingData<Comment>> get() = _commentsState

    fun loadPostComments(postId: Int) {
        viewModelScope.launch {
            getPostCommentsUseCase(postId)
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _commentsState.value = pagingData
                }
        }
    }
}
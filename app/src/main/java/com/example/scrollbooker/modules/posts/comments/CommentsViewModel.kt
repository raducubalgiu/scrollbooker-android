package com.example.scrollbooker.modules.posts.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.comment.data.remote.LikeCommentDto
import com.example.scrollbooker.entity.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.comment.domain.model.Comment
import com.example.scrollbooker.entity.comment.domain.useCase.CreateCommentUseCase
import com.example.scrollbooker.entity.comment.domain.useCase.GetPaginatedPostCommentsUseCase
import com.example.scrollbooker.entity.comment.domain.useCase.LikeCommentUseCase
import com.example.scrollbooker.entity.comment.domain.useCase.UnLikeCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPaginatedPostCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val unLikeCommentUseCase: UnLikeCommentUseCase
): ViewModel() {

    private val _postId = MutableStateFlow<Int?>(null)
    val postId = _postId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val commentsState: Flow<PagingData<Comment>> = postId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id ->
            getPostCommentsUseCase(id)
        }
        .cachedIn(viewModelScope)

    fun setPostId(newPostId: Int) {
        if(_postId.value != newPostId) {
            _postId.value = newPostId
        }
    }

    private val _newComments = MutableStateFlow<List<Comment>>(emptyList())
    val newComments: StateFlow<List<Comment>> = _newComments

    fun createComment(postId: Int, text: String, parentId: Int?) {
        viewModelScope.launch {
            val comment = createCommentUseCase(
                postId = postId,
                text = text,
                parentId = parentId
            )

            if(comment != null) {
                _newComments.value = listOf(comment) + _newComments.value
            }
        }

    }

    fun toggleLikeComment(likeCommentDto: LikeCommentDto) {
        viewModelScope.launch {
            val (postId, commentId, action) = likeCommentDto

            if(action == LikeCommentEnum.LIKE) {
                likeCommentUseCase(postId, commentId)
            } else {
                unLikeCommentUseCase(postId, commentId)
            }
        }
    }
}
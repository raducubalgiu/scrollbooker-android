package com.example.scrollbooker.components.customized.post.sheets.comments
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.entity.social.comment.domain.useCase.CreateCommentUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.GetPostCommentsUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.LikeCommentUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.UnLikeCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val unLikeCommentUseCase: UnLikeCommentUseCase
): ViewModel() {
    private val _postId = MutableStateFlow<Int?>(null)
    val postId = _postId.asStateFlow()

    private val _commentsRefreshTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val commentsState: Flow<PagingData<Comment>> = postId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id -> _commentsRefreshTrigger.map { trigger -> Pair(id, trigger) } }
        .flatMapLatest { (id, _) -> getPostCommentsUseCase(id) }
        .cachedIn(viewModelScope)


    fun setPostId(newPostId: Int) {
        if(_postId.value != newPostId) _postId.value = newPostId
    }

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun createComment(postId: Int, text: String, parentId: Int?) {
        _isSaving.value = true

        viewModelScope.launch {
            val response = createCommentUseCase(postId, text, parentId)

            response
                .onSuccess { comment ->
                    _commentsRefreshTrigger.value += 1
                    _isSaving.value = false
                }
                .onFailure { e ->
                    Timber.tag("Comments").e("ERROR: on creating new comment: ${e}")
                    _isSaving.value = false
                }
        }

    }
}
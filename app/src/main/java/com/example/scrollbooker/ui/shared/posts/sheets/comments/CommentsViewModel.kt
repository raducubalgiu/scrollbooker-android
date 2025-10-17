package com.example.scrollbooker.ui.shared.posts.sheets.comments
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.entity.social.comment.domain.useCase.CreateCommentUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.GetPostCommentsUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.LikeCommentUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.UnLikeCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class CommentPatch(
    val isLiked: Boolean,
    val likeCount: Int
)

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
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
        .flatMapLatest { id -> getPostCommentsUseCase(id) }
        .cachedIn(viewModelScope)

    fun setPostId(newPostId: Int) {
        if(_postId.value != newPostId) _postId.value = newPostId
    }

    private val _commentPatches = MutableStateFlow<Map<Int, CommentPatch>>(emptyMap())
    val commentPatches: StateFlow<Map<Int, CommentPatch>> = _commentPatches.asStateFlow()

    private val _inFlight = MutableStateFlow<Set<Int>>(emptySet())
    val inFlight: StateFlow<Set<Int>> = _inFlight.asStateFlow()

    private val _isSaving = MutableStateFlow<Boolean>(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _newCommentsByPost = MutableStateFlow<Map<Int, List<Comment>>>(emptyMap())

    fun newCommentsFor(postId: Int): StateFlow<List<Comment>> =
        _newCommentsByPost
            .map { it[postId].orEmpty() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun createComment(postId: Int, text: String, parentId: Int?) {
        _isSaving.value = true

        viewModelScope.launch {
            val response = createCommentUseCase(postId, text, parentId)

            response
                .onSuccess { comment ->
                    _newCommentsByPost.update { map ->
                        val current = map[postId].orEmpty()
                        map + (postId to (listOf(comment) + current))
                    }
                    _isSaving.value = false
                }
                .onFailure { e ->
                    Timber.tag("Comments").e("ERROR: on creating new comment: ${e}")
                    _isSaving.value = false
                }
        }

    }

    fun reconciliateNewWithPaging(postId: Int, pagesIds: Set<Int>) {
        _newCommentsByPost.update { map ->
            val current = map[postId].orEmpty()
            val filtered = current.filterNot { it.id in pagesIds }
            if(filtered.size == current.size) map else map + (postId to filtered)
        }
    }

    fun reconciliatePatched(commentId: Int, serverIsLiked: Boolean, serverLikeCount: Int) {
        _commentPatches.update { map ->
            val patch = map[commentId] ?: return
            if(patch.isLiked == serverIsLiked && patch.likeCount == serverLikeCount) map - commentId
            else map
        }
    }

    fun toggleLikeComment(comment: Comment, action: LikeCommentEnum) {
        val isActionLike = action == LikeCommentEnum.LIKE

        if (_inFlight.value.contains(comment.id)) return

        val delta = if (isActionLike) 1 else -1
        val newCount = (comment.likeCount + delta).coerceAtLeast(0)
        val patched = CommentPatch(
            isLiked = isActionLike,
            likeCount = newCount
        )

        _commentPatches.update { it + (comment.id to patched) }
        _inFlight.update { it + comment.id }

        viewModelScope.launch {
            val response = if (isActionLike) {
                likeCommentUseCase(comment.id)
            } else {
                unLikeCommentUseCase(comment.id)
            }

            response
                .onSuccess {
                    _inFlight.update { it - comment.id }
                }
                .onFailure { e ->
                    Timber.tag("Comments").e("ERROR: on toggling like for comment ${comment.id}: ${e}")

                    _commentPatches.update { map ->
                        val prev = map[comment.id]
                        if (prev != null) {
                            val rollback = CommentPatch(
                                isLiked = !isActionLike,
                                likeCount = (prev.likeCount - delta).coerceAtLeast(0)
                            )
                            map + (comment.id to rollback)
                        } else map
                    }
                    _inFlight.update { it - comment.id }
                }
        }
    }
}
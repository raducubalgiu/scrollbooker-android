package com.example.scrollbooker.components.customized.post.sheets.comments
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.entity.social.comment.domain.model.CommentUser
import com.example.scrollbooker.entity.social.comment.domain.useCase.CreateCommentUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.GetCommentRepliesUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.GetPostCommentsUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.LikeCommentUseCase
import com.example.scrollbooker.entity.social.comment.domain.useCase.UnLikeCommentUseCase
import com.example.scrollbooker.store.AuthDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.ZonedDateTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

data class CommentLikeState(
    val isLiked: Boolean,
    val likeCount: Int
)

data class ReplyTarget(
    val parentId: Int,
    val replyToCommentId: Int?,
    val replyToUsername: String
)

data class RepliesState(
    val items: List<Comment> = emptyList(),
    val page: Int = 0,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val isExpanded: Boolean = false
)

private const val REPLIES_PAGE_SIZE = 10

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getPostCommentsUseCase: GetPostCommentsUseCase,
    private val getCommentRepliesUseCase: GetCommentRepliesUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val unLikeCommentUseCase: UnLikeCommentUseCase,
    private val authDataStore: AuthDataStore
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

    private val _currentUser = MutableStateFlow<CommentUser?>(null)

    init {
        combine(
            authDataStore.getUserId(),
            authDataStore.getUserUsername(),
            authDataStore.getUserFullName()
        ) { id, username, fullName ->
            if (id == null || username == null) null
            else CommentUser(
                id = id,
                username = username,
                fullname = fullName ?: username,
                avatar = null,
                profession = ""
            )
        }.onEach { _currentUser.value = it }.launchIn(viewModelScope)
    }

    // ---- Reply targeting ----

    private val _replyTarget = MutableStateFlow<ReplyTarget?>(null)
    val replyTarget: StateFlow<ReplyTarget?> = _replyTarget.asStateFlow()

    fun setReplyTarget(comment: Comment) {
        _replyTarget.value = if (comment.parentId == null) {
            ReplyTarget(parentId = comment.id, replyToCommentId = null, replyToUsername = comment.user.username)
        } else {
            ReplyTarget(parentId = comment.parentId, replyToCommentId = comment.id, replyToUsername = comment.user.username)
        }
    }

    fun clearReplyTarget() {
        _replyTarget.value = null
    }

    // ---- Optimistic comment creation ----

    private val _pendingComments = MutableStateFlow<List<PendingComment>>(emptyList())
    val pendingComments: StateFlow<List<PendingComment>> = _pendingComments.asStateFlow()

    private var tempIdSeed = 0

    fun createComment(postId: Int, text: String) {
        val user = _currentUser.value ?: run {
            Timber.tag("Comments").e("ERROR: current user is not loaded yet")
            return
        }

        val target = _replyTarget.value
        val parentId = target?.parentId
        val replyToCommentId = target?.replyToCommentId

        val localId = UUID.randomUUID().toString()
        val tempId = --tempIdSeed

        val tempComment = Comment(
            id = tempId,
            text = text,
            user = user,
            postId = postId,
            repliesCount = 0,
            likeCount = 0,
            isLiked = false,
            likedByPostAuthor = false,
            parentId = parentId,
            replyToCommentId = replyToCommentId,
            createdAt = ZonedDateTime.now()
        )

        _pendingComments.update {
            listOf(PendingComment(localId, tempComment, parentId, replyToCommentId, PendingStatus.SENDING)) + it
        }

        if (parentId != null) {
            _repliesState.update { map ->
                val prev = map[parentId] ?: RepliesState()
                map + (parentId to prev.copy(isExpanded = true))
            }
        }

        clearReplyTarget()
        sendPendingComment(localId, postId, text, parentId, replyToCommentId)
    }

    fun retryComment(localId: String) {
        val pending = _pendingComments.value.find { it.localId == localId } ?: return

        _pendingComments.update { list ->
            list.map { if (it.localId == localId) it.copy(status = PendingStatus.SENDING) else it }
        }

        sendPendingComment(localId, pending.comment.postId, pending.comment.text, pending.parentId, pending.replyToCommentId)
    }

    fun discardPendingComment(localId: String) {
        _pendingComments.update { list -> list.filterNot { it.localId == localId } }
    }

    private fun sendPendingComment(localId: String, postId: Int, text: String, parentId: Int?, replyToCommentId: Int?) {
        viewModelScope.launch {
            createCommentUseCase(postId, text, parentId, replyToCommentId)
                .onSuccess { comment ->
                    _pendingComments.update { list ->
                        list.map { if (it.localId == localId) it.copy(comment = comment, status = PendingStatus.SENT) else it }
                    }
                }
                .onFailure { e ->
                    _pendingComments.update { list ->
                        list.map { if (it.localId == localId) it.copy(status = PendingStatus.FAILED) else it }
                    }
                    Timber.tag("Comments").e(e, "ERROR: on creating new comment")
                }
        }
    }

    // ---- Replies pagination ----

    private val _repliesState = MutableStateFlow<Map<Int, RepliesState>>(emptyMap())
    val repliesState: StateFlow<Map<Int, RepliesState>> = _repliesState.asStateFlow()

    fun toggleReplies(commentId: Int) {
        val current = _repliesState.value[commentId]

        if (current?.isExpanded == true) {
            _repliesState.update { it + (commentId to current.copy(isExpanded = false)) }
            return
        }

        _repliesState.update { it + (commentId to (current ?: RepliesState()).copy(isExpanded = true)) }

        if (current == null || (current.items.isEmpty() && current.hasMore)) {
            loadMoreReplies(commentId)
        }
    }

    fun loadMoreReplies(commentId: Int) {
        val postId = _postId.value ?: return
        val state = _repliesState.value[commentId] ?: RepliesState()

        if (state.isLoading || !state.hasMore) return

        _repliesState.update { it + (commentId to state.copy(isLoading = true, isExpanded = true)) }

        viewModelScope.launch {
            val nextPage = state.page + 1

            getCommentRepliesUseCase(postId, commentId, nextPage, REPLIES_PAGE_SIZE)
                .onSuccess { page ->
                    _repliesState.update { map ->
                        val prev = map[commentId] ?: RepliesState()
                        map + (commentId to prev.copy(
                            items = prev.items + page.items,
                            page = nextPage,
                            hasMore = page.hasMore,
                            isLoading = false,
                            isExpanded = true
                        ))
                    }
                }
                .onFailure { e ->
                    _repliesState.update { map ->
                        val prev = map[commentId] ?: RepliesState()
                        map + (commentId to prev.copy(isLoading = false))
                    }
                    Timber.tag("Comments").e(e, "ERROR: on loading replies for comment $commentId")
                }
        }
    }

    // ---- Optimistic like/unlike ----

    private val _likeOverrides = MutableStateFlow<Map<Int, CommentLikeState>>(emptyMap())
    val likeOverrides: StateFlow<Map<Int, CommentLikeState>> = _likeOverrides.asStateFlow()

    private val _likeSaving = MutableStateFlow<Set<Int>>(emptySet())
    val likeSaving: StateFlow<Set<Int>> = _likeSaving.asStateFlow()

    fun toggleLike(comment: Comment, action: LikeCommentEnum) {
        val commentId = comment.id
        if (_likeSaving.value.contains(commentId)) return

        val previous = _likeOverrides.value[commentId]
            ?: CommentLikeState(comment.isLiked, comment.likeCount)

        val newIsLiked = action == LikeCommentEnum.LIKE
        if (newIsLiked == previous.isLiked) return

        val newLikeCount = (previous.likeCount + if (newIsLiked) 1 else -1).coerceAtLeast(0)

        _likeSaving.update { it + commentId }
        _likeOverrides.update { it + (commentId to CommentLikeState(newIsLiked, newLikeCount)) }

        viewModelScope.launch {
            val response = if (newIsLiked) likeCommentUseCase(commentId) else unLikeCommentUseCase(commentId)

            response.onFailure { e ->
                _likeOverrides.update { it + (commentId to previous) }
                Timber.tag("Comments").e(e, "ERROR: on like/unlike comment $commentId")
            }

            _likeSaving.update { it - commentId }
        }
    }
}

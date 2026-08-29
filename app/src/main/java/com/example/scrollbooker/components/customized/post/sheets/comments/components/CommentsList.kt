package com.example.scrollbooker.components.customized.post.sheets.comments.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.components.customized.post.sheets.comments.CommentLikeState
import com.example.scrollbooker.components.customized.post.sheets.comments.PendingComment
import com.example.scrollbooker.components.customized.post.sheets.comments.PendingStatus
import com.example.scrollbooker.components.customized.post.sheets.comments.RepliesState
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.navigation.navigators.UserProfileParam

fun Comment.withLikeOverride(likeOverrides: Map<Int, CommentLikeState>): Comment {
    val override = likeOverrides[id] ?: return this
    return copy(isLiked = override.isLiked, likeCount = override.likeCount)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommentsList(
    comments: LazyPagingItems<Comment>,
    pendingComments: List<PendingComment>,
    likeOverrides: Map<Int, CommentLikeState>,
    repliesState: Map<Int, RepliesState>,
    onLikeClick: (comment: Comment, action: LikeCommentEnum) -> Unit,
    onReplyClick: (comment: Comment) -> Unit,
    onToggleReplies: (commentId: Int) -> Unit,
    onLoadMoreReplies: (commentId: Int) -> Unit,
    onRetryComment: (localId: String) -> Unit,
    onDiscardComment: (localId: String) -> Unit,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    val topLevelPending = pendingComments.filter { it.parentId == null }

    fun repliesFor(commentId: Int) = pendingComments.filter { it.parentId == commentId }

    Box(Modifier.fillMaxSize()) {
        if(comments.itemCount == 0 && topLevelPending.isEmpty()) {
            EmptyScreen(
                icon = painterResource(R.drawable.ic_comment_outline),
                message = stringResource(R.string.notFoundComments),
            )
        } else {
            LazyColumn(Modifier.fillMaxSize(), state = listState) {
                items(topLevelPending, key = { it.localId }) { pending ->
                    if (pending.status == PendingStatus.SENT) {
                        val comment = pending.comment
                        Column {
                            CommentItem(
                                comment = comment.withLikeOverride(likeOverrides),
                                onLikeClick = onLikeClick,
                                onReplyClick = onReplyClick,
                                onNavigateToUserProfile = onNavigateToUserProfile
                            )
                            RepliesSection(
                                comment = comment,
                                repliesState = repliesState[comment.id] ?: RepliesState(),
                                pendingReplies = repliesFor(comment.id),
                                likeOverrides = likeOverrides,
                                onToggle = { onToggleReplies(comment.id) },
                                onLoadMore = { onLoadMoreReplies(comment.id) },
                                onLikeClick = onLikeClick,
                                onReplyClick = onReplyClick,
                                onRetryComment = onRetryComment,
                                onDiscardComment = onDiscardComment,
                                onNavigateToUserProfile = onNavigateToUserProfile
                            )
                        }
                    } else {
                        PendingCommentItem(
                            pending = pending,
                            onRetry = { onRetryComment(pending.localId) },
                            onDiscard = { onDiscardComment(pending.localId) }
                        )
                    }
                }

                items(comments.itemCount) { index ->
                    comments[index]?.let { comment ->
                        Column {
                            CommentItem(
                                comment = comment.withLikeOverride(likeOverrides),
                                onLikeClick = onLikeClick,
                                onReplyClick = onReplyClick,
                                onNavigateToUserProfile = onNavigateToUserProfile
                            )
                            RepliesSection(
                                comment = comment,
                                repliesState = repliesState[comment.id] ?: RepliesState(),
                                pendingReplies = repliesFor(comment.id),
                                likeOverrides = likeOverrides,
                                onToggle = { onToggleReplies(comment.id) },
                                onLoadMore = { onLoadMoreReplies(comment.id) },
                                onLikeClick = onLikeClick,
                                onReplyClick = onReplyClick,
                                onRetryComment = onRetryComment,
                                onDiscardComment = onDiscardComment,
                                onNavigateToUserProfile = onNavigateToUserProfile
                            )
                        }
                    }
                }

                item {
                    when (comments.loadState.append) {
                        is LoadState.Loading -> LoadMoreSpinner()
                        is LoadState.Error -> "Something went wrong"
                        is LoadState.NotLoading -> Unit
                    }
                }
            }
        }
    }
}

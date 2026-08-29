package com.example.scrollbooker.components.customized.post.sheets.comments.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
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
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.navigation.navigators.UserProfileParam

private fun Comment.withLikeOverride(likeOverrides: Map<Int, CommentLikeState>): Comment {
    val override = likeOverrides[id] ?: return this
    return copy(isLiked = override.isLiked, likeCount = override.likeCount)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommentsList(
    comments: LazyPagingItems<Comment>,
    pendingComments: List<PendingComment>,
    likeOverrides: Map<Int, CommentLikeState>,
    onLikeClick: (comment: Comment, action: LikeCommentEnum) -> Unit,
    onRetryComment: (localId: String) -> Unit,
    onDiscardComment: (localId: String) -> Unit,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit,
    listState: LazyListState = rememberLazyListState()
) {
    Box(Modifier.fillMaxSize()) {
        if(comments.itemCount == 0 && pendingComments.isEmpty()) {
            EmptyScreen(
                icon = painterResource(R.drawable.ic_comment_outline),
                message = stringResource(R.string.notFoundComments),
            )
        } else {
            LazyColumn(Modifier.fillMaxSize(), state = listState) {
                items(pendingComments, key = { it.localId }) { pending ->
                    if (pending.status == PendingStatus.SENT) {
                        CommentItem(
                            comment = pending.comment.withLikeOverride(likeOverrides),
                            onLikeClick = onLikeClick,
                            onNavigateToUserProfile = onNavigateToUserProfile
                        )
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
                        CommentItem(
                            comment = comment.withLikeOverride(likeOverrides),
                            onLikeClick = onLikeClick,
                            onNavigateToUserProfile = onNavigateToUserProfile
                        )
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
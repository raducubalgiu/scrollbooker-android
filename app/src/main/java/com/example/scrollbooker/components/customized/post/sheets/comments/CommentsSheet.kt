package com.example.scrollbooker.components.customized.post.sheets.comments
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.customized.post.sheets.comments.components.CommentFooter
import com.example.scrollbooker.components.customized.post.sheets.comments.components.CommentsList
import com.example.scrollbooker.navigation.navigators.UserProfileParam

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommentsSheet(
    postId: Int,
    onClose: () -> Unit,
    onNavigateToUserProfile: (param: UserProfileParam) -> Unit
) {
    val viewModel: CommentsViewModel = hiltViewModel()
    val comments = viewModel.commentsState.collectAsLazyPagingItems()
    val pendingComments by viewModel.pendingComments.collectAsStateWithLifecycle()
    val likeOverrides by viewModel.likeOverrides.collectAsStateWithLifecycle()
    val repliesState by viewModel.repliesState.collectAsStateWithLifecycle()
    val replyTarget by viewModel.replyTarget.collectAsStateWithLifecycle()

    val refreshState = comments.loadState.refresh
    val isInitialLoading = refreshState is LoadState.Loading && comments.itemCount == 0

    val listState = rememberLazyListState()

    LaunchedEffect(postId) {
        viewModel.setPostId(newPostId = postId)
    }

    val topPendingCommentId = pendingComments.firstOrNull { it.parentId == null }?.localId

    LaunchedEffect(topPendingCommentId) {
        if (topPendingCommentId != null) {
            listState.scrollToItem(0)
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(fraction = 0.85f)
    ) {
        SheetHeader(
            title = stringResource(R.string.comments),
            onClose = onClose
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                when {
                    isInitialLoading -> LoadingScreen()
                    refreshState is LoadState.Error -> ErrorScreen()
                    else -> {
                        CommentsList(
                            comments = comments,
                            pendingComments = pendingComments,
                            likeOverrides = likeOverrides,
                            repliesState = repliesState,
                            onLikeClick = { comment, action -> viewModel.toggleLike(comment, action) },
                            onReplyClick = { comment -> viewModel.setReplyTarget(comment) },
                            onToggleReplies = { commentId -> viewModel.toggleReplies(commentId) },
                            onLoadMoreReplies = { commentId -> viewModel.loadMoreReplies(commentId) },
                            onRetryComment = { localId -> viewModel.retryComment(localId) },
                            onDiscardComment = { localId -> viewModel.discardPendingComment(localId) },
                            onNavigateToUserProfile = onNavigateToUserProfile,
                            listState = listState
                        )
                    }
                }
            }

            CommentFooter(
                replyTarget = replyTarget,
                onCancelReply = { viewModel.clearReplyTarget() },
                onCreateComment = { text ->
                    viewModel.createComment(postId = postId, text = text)
                }
            )
        }
    }
}

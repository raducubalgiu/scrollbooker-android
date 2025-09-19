package com.example.scrollbooker.ui.modules.posts.sheets.comments
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.ui.modules.posts.sheets.comments.components.CommentItemShimmer
import com.example.scrollbooker.ui.modules.posts.sheets.comments.components.CommentsList

@Composable
fun CommentsSheet(
    postId: Int,
    isSheetVisible: Boolean,
    onClose: () -> Unit
) {
    val viewModel: CommentsViewModel = hiltViewModel()

    LaunchedEffect(postId) {
        viewModel.setPostId(newPostId = postId)
    }

    val comments = viewModel.commentsState.collectAsLazyPagingItems()
    val newComments by viewModel.newComments.collectAsState()

    SheetHeader(
        title = stringResource(R.string.comments),
        onClose = onClose
    )

    if(isSheetVisible) {
        when (comments.loadState.refresh) {
            is LoadState.Loading -> repeat(6) {
                repeat(7) {
                    CommentItemShimmer()
                }
            }
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> CommentsList(
                comments = comments,
                newComments = newComments,
                onLike = { viewModel.toggleLikeComment(it) },
                onCreateComment = {
                    viewModel.createComment(
                        postId = postId,
                        text = it.text,
                        parentId = it.parentId
                    )
                }
            )
        }
    }
}
package com.example.scrollbooker.modules.post.comments
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.entity.comment.data.remote.LikeCommentDto
import com.example.scrollbooker.entity.comment.domain.model.Comment
import com.example.scrollbooker.entity.comment.domain.model.CreateComment

@Composable
fun CommentsSheet(
    comments: LazyPagingItems<Comment>,
    newComments: List<Comment>,
    isSheetVisible: Boolean,
    onCreateComment: (CreateComment) -> Unit,
    onClose: () -> Unit,
    onLike: (LikeCommentDto) -> Unit
) {
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
                onLike = onLike,
                onCreateComment = onCreateComment
            )
        }
    }
}
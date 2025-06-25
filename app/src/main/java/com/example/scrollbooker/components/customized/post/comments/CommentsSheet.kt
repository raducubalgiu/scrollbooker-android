package com.example.scrollbooker.components.customized.post.comments
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.EmptyScreen
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.core.shimmer.Shimmer
import com.example.scrollbooker.shared.comment.data.remote.LikeCommentDto
import com.example.scrollbooker.shared.comment.domain.model.Comment
import com.example.scrollbooker.shared.comment.domain.model.CreateComment

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
                Shimmer()
            }
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> {
                if(comments.itemCount == 0) {
                    EmptyScreen(
                        icon = Icons.Outlined.ModeComment,
                        message = stringResource(R.string.notFoundComments),
                    )
                } else {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        LazyColumn(Modifier.weight(1f)) {
                            items(newComments) { comment ->
                                CommentItem(comment, onLike)
                            }

                            items(comments.itemCount) { index ->
                                comments[index]?.let { comment ->
                                    CommentItem(comment, onLike)
                                }
                            }

                            item {
                                when(comments.loadState.append) {
                                    is LoadState.Loading -> LoadMoreSpinner()
                                    is LoadState.Error -> "Something went wrong"
                                    is LoadState.NotLoading -> Unit
                                }
                            }
                        }

                        CommentFooter(
                            onCreateComment = onCreateComment
                        )
                    }
                }
            }
        }
    }
}
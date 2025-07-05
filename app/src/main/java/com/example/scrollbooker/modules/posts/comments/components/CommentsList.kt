package com.example.scrollbooker.modules.posts.comments.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.comment.data.remote.LikeCommentDto
import com.example.scrollbooker.entity.comment.domain.model.Comment
import com.example.scrollbooker.entity.comment.domain.model.CreateComment

@Composable
fun CommentsList(
    comments: LazyPagingItems<Comment>,
    newComments: List<Comment>,
    onLike: (LikeCommentDto) -> Unit,
    onCreateComment: (CreateComment) -> Unit
) {
    if(comments.itemCount == 0) {
        EmptyScreen(
            icon = painterResource(R.drawable.ic_comment_outline),
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
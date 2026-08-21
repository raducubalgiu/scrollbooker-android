package com.example.scrollbooker.components.customized.post.sheets.comments.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.EmptyScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.entity.social.comment.data.remote.LikeCommentEnum
import com.example.scrollbooker.entity.social.comment.domain.model.Comment

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommentsList(
    comments: LazyPagingItems<Comment>,
    onLikeClick: (comment: Comment, action: LikeCommentEnum) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        if(comments.itemCount == 0) {
            EmptyScreen(
                icon = painterResource(R.drawable.ic_comment_outline),
                message = stringResource(R.string.notFoundComments),
            )
        } else {
            LazyColumn(Modifier.fillMaxSize()) {
                items(comments.itemCount) { index ->
                    comments[index]?.let { comment ->
                        CommentItem(
                            comment = comment,
                            onLikeClick = onLikeClick
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
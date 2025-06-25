package com.example.scrollbooker.components.customized.post.comments

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.shared.comment.domain.model.Comment

//@Composable
//fun CommentsLoadedList(
//    comments: LazyPagingItems<Comment>
//) {
//    LazyColumn {
//
//
//        items(comments.itemCount) { index ->
//            comments[index]?.let { comment ->
//                CommentItem(comment)
//            }
//        }
//    }
//
//    when(comments.loadState.append) {
//        is LoadState.Loading -> LoadMoreSpinner()
//        is LoadState.Error -> "Something went wrong"
//        is LoadState.NotLoading -> Unit
//    }
//}
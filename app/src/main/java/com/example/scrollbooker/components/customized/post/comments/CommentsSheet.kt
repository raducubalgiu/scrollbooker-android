package com.example.scrollbooker.components.customized.post.comments
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.EmptyScreen
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.LoadingScreen

@Composable
fun CommentsSheet(postId: Int) {
    val viewModel: CommentsViewModel = hiltViewModel()

    LaunchedEffect(postId) {
        viewModel.loadPostComments(postId)
    }

    val comments = viewModel.commentsPosts.collectAsLazyPagingItems()

    when (comments.loadState.refresh) {
        is LoadState.Loading -> LoadingScreen()
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
                    Column(Modifier.weight(1f)) {
                        CommentsLoadedList(comments)
                    }

                    CommentFooter()
                }
            }
        }
    }
}
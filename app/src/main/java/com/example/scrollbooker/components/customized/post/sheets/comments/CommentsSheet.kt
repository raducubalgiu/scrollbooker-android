package com.example.scrollbooker.components.customized.post.sheets.comments
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.components.customized.post.sheets.comments.components.CommentFooter
import com.example.scrollbooker.components.customized.post.sheets.comments.components.CommentsList

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CommentsSheet(
    postId: Int,
    onClose: () -> Unit
) {
    val viewModel: CommentsViewModel = hiltViewModel()
    val comments = viewModel.commentsState.collectAsLazyPagingItems()

    val refreshState = comments.loadState.refresh
    val isInitialLoading = refreshState is LoadState.Loading && comments.itemCount == 0

    LaunchedEffect(postId) {
        viewModel.setPostId(newPostId = postId)
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
                            onLikeClick = { comment, action -> },
                        )
                    }
                }
            }

            CommentFooter(
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
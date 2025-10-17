package com.example.scrollbooker.ui.shared.posts.sheets.comments
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.shared.posts.sheets.comments.components.CommentFooter
import com.example.scrollbooker.ui.shared.posts.sheets.comments.components.CommentsList
import timber.log.Timber

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
    val newComments by viewModel.newCommentsFor(postId).collectAsState()
    val patches by viewModel.commentPatches.collectAsState()
    val inFlight by viewModel.inFlight.collectAsState()

    LaunchedEffect(comments.loadState.refresh, comments.itemCount) {
        if(comments.loadState.refresh is LoadState.NotLoading) {
            val ids = comments.itemSnapshotList.items.map { it.id }.toSet()
            viewModel.reconciliateNewWithPaging(postId, ids)
        }
    }

    Timber.tag("PATCH").e("PATCHES: $patches")

    Scaffold(
        topBar = {
            SheetHeader(
                modifier = Modifier.padding(bottom = BasePadding),
                title = stringResource(R.string.comments),
                onClose = onClose
            )
        },
        bottomBar = {
            CommentFooter(onCreateComment = {
                viewModel.createComment(
                    postId = postId,
                    text = it.text,
                    parentId = it.parentId
                )
            })
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            if(isSheetVisible) {
                when (comments.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.NotLoading -> {
                        CommentsList(
                            comments = comments,
                            newComments = newComments,
                            inFlight = inFlight,
                            patches = patches,
                            onToggleLike = { comment, action ->
                                viewModel.toggleLikeComment(comment, action)
                            },
                        )
                    }
                }
            }
        }
    }
}
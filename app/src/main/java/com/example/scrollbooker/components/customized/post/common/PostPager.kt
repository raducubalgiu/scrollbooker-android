package com.example.scrollbooker.components.customized.post.common

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.customized.post.comments.CommentsSheet
import com.example.scrollbooker.components.customized.post.comments.CommentsViewModel
import com.example.scrollbooker.components.customized.post.reviews.ReviewsListSheet
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.shared.post.domain.model.Post
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PostPager(
    posts: LazyPagingItems<Post>,
    pagerState: PagerState,
    isVisibleTab: Boolean,
    paddingBottom: Dp = 90.dp,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: CommentsViewModel = hiltViewModel()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(PostSheetsContent.None) }
    val isDarkTheme = isSystemInDarkTheme()

    if(sheetContent != PostSheetsContent.None) {
        ModalBottomSheet(
            dragHandle = null,
            onDismissRequest = { sheetContent = PostSheetsContent.None },
            containerColor = if(isDarkTheme) SurfaceBG else Background,
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Column(modifier = Modifier
                .statusBarsPadding()
                .fillMaxHeight(1f)
            ) {
                when (val content = sheetContent) {
                    is PostSheetsContent.ReviewsSheet -> {
                        ReviewsListSheet(
                            postId = content.postId
                        )
                    }
                    is PostSheetsContent.CommentsSheet -> {
                        viewModel.setPostId(newPostId = content.postId)

                        val comments = viewModel.commentsState.collectAsLazyPagingItems()
                        val newComments by viewModel.newComments.collectAsState()

                        CommentsSheet(
                            comments = comments,
                            newComments = newComments,
                            isSheetVisible = sheetState.isVisible,
                            onCreateComment = {
                                viewModel.createComment(
                                    postId = content.postId,
                                    text =  it.text,
                                    parentId = it.parentId
                                )
                            },
                            onClose = {
                                sheetContent = PostSheetsContent.None
                                coroutineScope.launch { sheetState.hide() }
                            },
                            onLike = { viewModel.toggleLikeComment(it) }
                        )
                    }
                    is PostSheetsContent.CalendarSheet -> Unit
                    is PostSheetsContent.None -> Unit
                }
            }
        }
    }

    VerticalPager(
        state = pagerState,
        beyondViewportPageCount = 1,
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = paddingBottom)
    ) { page ->
        when(posts.loadState.append) {
            is LoadState.Error -> "Something went wrong"
            is LoadState.Loading -> LoadMoreSpinner()
            is LoadState.NotLoading -> {
                val post = posts[page]

                if(post != null) {
                    PostItem(
                        post = post,
                        playWhenReady = pagerState.currentPage == page && isVisibleTab,
                        onOpenReviews = {
                            sheetContent = PostSheetsContent.ReviewsSheet(post.id)
                            coroutineScope.launch {
                                sheetState.show()
                            }
                        },
                        onOpenComments = {
                            sheetContent = PostSheetsContent.CommentsSheet(post.id)
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        }
                    )
                }
            }
        }
    }
}
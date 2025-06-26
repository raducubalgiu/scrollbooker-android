package com.example.scrollbooker.modules.posts

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
import com.example.scrollbooker.modules.reviews.ReviewsViewModel
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.modules.posts.comments.CommentsSheet
import com.example.scrollbooker.modules.posts.comments.CommentsViewModel
import com.example.scrollbooker.modules.posts.common.PostItem
import com.example.scrollbooker.modules.posts.common.PostSheetsContent
import com.example.scrollbooker.modules.posts.reviews.ReviewsListSheet
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.SurfaceBG
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PostsPager(
    posts: LazyPagingItems<Post>,
    pagerState: PagerState,
    isVisibleTab: Boolean,
    paddingBottom: Dp = 90.dp,
    modifier: Modifier = Modifier
) {
    val pagerViewModel: PostsPagerViewModel = hiltViewModel()
    val commentsViewModel: CommentsViewModel = hiltViewModel()
    val reviewsViewModel: ReviewsViewModel = hiltViewModel()

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(PostSheetsContent.None) }
    val isDarkTheme = isSystemInDarkTheme()

    fun handleClose() {
        sheetContent = PostSheetsContent.None
        coroutineScope.launch { sheetState.hide() }
    }

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
                .fillMaxHeight(1f)) {
                when (val content = sheetContent) {
                    is PostSheetsContent.ReviewsSheet -> {
                        reviewsViewModel.setUserId(userId = content.userId)

                        ReviewsListSheet(
                            viewModel = reviewsViewModel,
                            onClose = { handleClose() },
                            onRatingClick = {
                                reviewsViewModel.toggleRatings(it)
                            }
                        )
                    }
                    is PostSheetsContent.CommentsSheet -> {
                        commentsViewModel.setPostId(newPostId = content.postId)

                        CommentsSheet(
                            viewModel = commentsViewModel,
                            postId = content.postId,
                            isSheetVisible = sheetState.isVisible,
                            onClose = { handleClose() }
                        )
                    }
                    is PostSheetsContent.CalendarSheet -> Unit
                    is PostSheetsContent.None -> Unit
                }
            }
        }
    }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        sheetContent = targetSheet
        coroutineScope.launch {
            sheetState.show()
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
                        viewModel = pagerViewModel,
                        post = post,
                        playWhenReady = pagerState.currentPage == page && isVisibleTab,
                        onOpenReviews = { handleOpenSheet(PostSheetsContent.ReviewsSheet(post.user.id)) },
                        onOpenComments = { handleOpenSheet(PostSheetsContent.CommentsSheet(post.id)) }
                    )
                }
            }
        }
    }
}
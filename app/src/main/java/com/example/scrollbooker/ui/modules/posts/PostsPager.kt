package com.example.scrollbooker.ui.modules.posts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.shared.posts.components.PostItem
import com.example.scrollbooker.ui.shared.posts.components.PostShimmer
import com.example.scrollbooker.ui.shared.posts.sheets.LocationSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent
import com.example.scrollbooker.ui.shared.posts.sheets.ReviewsListSheet
import com.example.scrollbooker.ui.shared.posts.sheets.calendar.PostCalendarSheet
import com.example.scrollbooker.ui.shared.posts.sheets.comments.CommentsSheet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoroutinesApi::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun PostsPager(
    posts: LazyPagingItems<Post>,
    isVisibleTab: Boolean,
    paddingBottom: Dp = 90.dp,
    onDisplayBottomBar: (Boolean) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0) { posts.itemCount }
    val pagerViewModel: PostsPagerViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var sheetContent by remember { mutableStateOf<PostSheetsContent>(PostSheetsContent.None) }

    fun handleClose() {
        sheetContent = PostSheetsContent.None
        scope.launch { sheetState.hide() }
    }

    LaunchedEffect(Unit) {
        var previousPage = pagerState.currentPage
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { currentPage ->
                val shouldShow = currentPage == 0 || currentPage < previousPage
                onDisplayBottomBar(shouldShow)
                previousPage = currentPage
            }
    }

    if(sheetState.isVisible) {
        Sheet(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 50.dp),
            onClose = { sheetContent = PostSheetsContent.None },
            sheetState = sheetState,
        ) {
            Column(modifier = Modifier.fillMaxHeight(1f)) {
                when (val content = sheetContent) {
                    is PostSheetsContent.ReviewsSheet -> {
                        ReviewsListSheet(
                            userId = content.userId,
                            onClose = { handleClose() },
                        )
                    }
                    is PostSheetsContent.CommentsSheet -> {
                        CommentsSheet(
                            postId = content.postId,
                            isSheetVisible = sheetState.isVisible,
                            onClose = { handleClose() }
                        )
                    }
                    is PostSheetsContent.CalendarSheet -> PostCalendarSheet()
                    is PostSheetsContent.LocationSheet -> {
                        LocationSheet(
                            pagerViewModel = pagerViewModel,
                            onClose = { handleClose() },
                            businessId = content.businessId
                        )
                    }
                    is PostSheetsContent.None -> Unit
                }
            }
        }
    }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        sheetContent = targetSheet

        scope.launch { sheetState.show() }
    }

    posts.apply {
        when(loadState.refresh) {
            is LoadState.Loading -> PostShimmer()
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> {
                VerticalPager(
                    state = pagerState,
                    beyondViewportPageCount = 1,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = paddingBottom)
                ) { page ->
                    when(posts.loadState.append) {
                        is LoadState.Error -> "Something went wrong"
                        is LoadState.Loading -> LoadMoreSpinner()
                        is LoadState.NotLoading -> {
                            val post = posts[page]

                            LaunchedEffect(post) {
                                if(post != null) {
                                    pagerViewModel.setInitialState(
                                        postId = post.id,
                                        isLiked = post.userActions.isLiked,
                                        likeCount = post.counters.likeCount,
                                        isBookmarked = post.userActions.isBookmarked,
                                        bookmarkCount = post.counters.bookmarkCount
                                    )
                                }
                            }

                            if(post != null) {
                                PostItem(
                                    viewModel = pagerViewModel,
                                    post = post,
                                    playWhenReady = pagerState.currentPage == page && isVisibleTab,
                                    onOpenReviews = { handleOpenSheet(PostSheetsContent.ReviewsSheet(post.user.id)) },
                                    onOpenComments = { handleOpenSheet(PostSheetsContent.CommentsSheet(post.id)) },
                                    onOpenCalendar = { handleOpenSheet(PostSheetsContent.CalendarSheet(post.user.id)) },
                                    onOpenLocation = { handleOpenSheet(PostSheetsContent.LocationSheet(post.businessId)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
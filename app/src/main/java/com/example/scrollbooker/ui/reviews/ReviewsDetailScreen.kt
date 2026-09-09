package com.example.scrollbooker.ui.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.customized.post.PostDetailSkeleton
import com.example.scrollbooker.components.customized.post.PostVerticalPager
import com.example.scrollbooker.components.customized.post.handlePostSheetAction
import com.example.scrollbooker.components.customized.post.sheets.PostSheetActionEnum
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsHost
import com.example.scrollbooker.components.customized.post.sheets.rememberPostSheetsState
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.theme.BackgroundDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsDetailScreen(
    reviewTabKey: String,
    reviewIndex: Int,
    viewModel: ReviewsViewModel,
    onBack: () -> Unit,
    onNavigateToUserProfile: (UserProfileParam) -> Unit,
    onNavigateToBooking: (Product, BookingSourceEnum) -> Unit,
    onNavigateToEditPost: (Int) -> Unit
) {
    val userPausedSet by viewModel.userPausedPostIds.collectAsStateWithLifecycle()

    val detailScopeKey = PostViewSourceEnum.VIDEO_REVIEWS.key
    val videoReviews = viewModel.videoReviews.collectAsLazyPagingItems()

    DisposableEffect(detailScopeKey) {
        viewModel.setDetailScreenActive(true, detailScopeKey, reviewIndex) { idx ->
            if (idx in 0 until videoReviews.itemCount) videoReviews.peek(idx) else null
        }
        onDispose {
            viewModel.setDetailScreenActive(false, detailScopeKey, reviewIndex) { idx ->
                if (idx in 0 until videoReviews.itemCount) videoReviews.peek(idx) else null
            }
            viewModel.onDetailSessionFinished(detailScopeKey)
        }
    }

    val postSheets = rememberPostSheetsState()

    PostSheetsHost(
        state = postSheets,
        onNavigateToBooking = { product -> onNavigateToBooking(product, BookingSourceEnum.VIDEO_REVIEWS) },
        onNavigateToEditPost = { onNavigateToEditPost(it) },
        onPostDeleted = { viewModel.refreshAfterPostDeleted() },
        onNavigateToUserProfile = onNavigateToUserProfile
    )

    val hasData = remember(videoReviews.itemCount) { videoReviews.itemCount > 0 }

    if (!hasData) {
        PostDetailSkeleton(onBack = onBack)
        return
    }

    key(reviewIndex) {
        val pagerState = rememberPagerState(initialPage = reviewIndex) { videoReviews.itemCount }

        val currentPost by remember(pagerState) {
            derivedStateOf {
                val currentPage = pagerState.currentPage
                if (currentPage in 0 until videoReviews.itemCount) videoReviews.peek(currentPage) else null
            }
        }

        LaunchedEffect(pagerState.settledPage) {
            viewModel.onPostSettled(
                scopeKey = detailScopeKey,
                index = pagerState.settledPage,
                getPost = { idx -> if (idx in 0 until videoReviews.itemCount) videoReviews.peek(idx) else null }
            )
        }

        Scaffold(
            containerColor = BackgroundDark,
            topBar = {
                Header(
                    onBack = onBack,
                    icon = Icons.Default.Close,
                    iconSize = 30.dp,
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundDark)
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                PostVerticalPager(
                    pagerState = pagerState,
                    items = videoReviews,
                    getPlayer = { p -> viewModel.getPlayerForIndex(detailScopeKey, p) },
                    userPausedPostIds = userPausedSet,
                    observePostUi = viewModel::observePostUi,
                    onTogglePlay = { p -> viewModel.togglePlayPause(detailScopeKey, p) },
                    onLike = { viewModel.toggleLike(it) },
                    onBookmark = { viewModel.toggleBookmark(it) },
                    onShare = { post, channel -> viewModel.sharePost(post, channel) },
                    onAction = { action, post -> handlePostSheetAction(action, post, postSheets::open) },
                    onNavigateToUserProfile = onNavigateToUserProfile,
                    onNavigateToReviews = { onBack() },
                    showBookButton = false,
                    modifier = Modifier.weight(1f)
                )

                MainButton(
                    modifier = Modifier.padding(
                        vertical = SpacingS,
                        horizontal = BasePadding
                    ),
                    contentPadding = PaddingValues(12.dp),
                    onClick = {
                        currentPost?.let {
                            handlePostSheetAction(
                                action = PostSheetActionEnum.OPEN_LINKED_PRODUCTS,
                                post = it,
                                handleOpenSheet = postSheets::open
                            )
                        }
                    },
                    title = stringResource(R.string.bookNow),
                )
            }
        }
    }
}

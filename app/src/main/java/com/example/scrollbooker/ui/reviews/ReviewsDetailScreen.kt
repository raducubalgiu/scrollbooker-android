package com.example.scrollbooker.ui.reviews

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.customized.post.PostPlayerWithThumbnail
import com.example.scrollbooker.components.customized.post.components.EndOfFeedPager
import com.example.scrollbooker.components.customized.post.components.PostOverlay
import com.example.scrollbooker.components.customized.post.components.VideoScrubber
import com.example.scrollbooker.components.customized.post.handlePostSheetAction
import com.example.scrollbooker.components.customized.post.sheets.PostSheetActionEnum
import com.example.scrollbooker.components.customized.post.sheets.PostSheets
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent.None
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.sharePost
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val userPausedSet by viewModel.userPausedPostIds.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(None) }

    if (sheetContent != None) {
        key(sheetContent) {
            PostSheets(
                sheetState = sheetState,
                sheetContent = sheetContent,
                onClose = {
                    scope.launch {
                        sheetState.hide()
                        sheetContent = None
                    }
                },
                onNavigateToBooking = { product ->
                    onNavigateToBooking(product, BookingSourceEnum.VIDEO_REVIEWS)
                },
                onNavigateToEditPost = { onNavigateToEditPost(it) },
                onOpenStatisticsSheet = {
                    scope.launch {
                        sheetState.hide()
                        sheetContent = PostSheetsContent.StatisticsSheet(it)
                    }
                },
                onOpenDeleteConfirm = {
                    scope.launch {
                        sheetState.hide()
                        sheetContent = PostSheetsContent.DeletePostSheet(it)
                    }
                },
                onPostDeleted = {
                    scope.launch {
                        sheetState.hide()
                        sheetContent = None
                        viewModel.refreshAfterPostDeleted()
                    }
                },
                onNavigateToUserProfile = onNavigateToUserProfile
            )
        }
    }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        scope.launch {
            sheetState.show()
            sheetContent = targetSheet
        }
    }

    val hasData = remember(videoReviews.itemCount) { videoReviews.itemCount > 0 }

    if (!hasData) {
        ReviewsDetailSkeleton(onBack = onBack)
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

        val fling = PagerDefaults.flingBehavior(
            state = pagerState,
            pagerSnapDistance = PagerSnapDistance.atMost(1),
            decayAnimationSpec = rememberSplineBasedDecay(),
            snapAnimationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessHigh
            )
        )

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
                EndOfFeedPager(
                    pagerState = pagerState,
                    isAtLastPage = { videoReviews.itemCount > 0 && pagerState.currentPage == videoReviews.itemCount - 1 },
                    modifier = Modifier.weight(1f),
                ) { pagerModifier ->
                    VerticalPager(
                        state = pagerState,
                        overscrollEffect = null,
                        flingBehavior = fling,
                        pageSize = PageSize.Fill,
                        pageSpacing = 0.dp,
                        beyondViewportPageCount = 1,
                        modifier = pagerModifier,
                    ) { page ->
                        val post = videoReviews.getOrNull(page) ?: return@VerticalPager

                        val player by remember(detailScopeKey, page) {
                            derivedStateOf { viewModel.getPlayerForIndex(detailScopeKey, page) }
                        }

                        val postActionState by viewModel.observePostUi(post.id).collectAsStateWithLifecycle()
                        val postUi = remember(post, postActionState) {
                            post.copy(
                                userActions = post.userActions.applyUiState(postActionState),
                                counters = post.counters.applyUiState(postActionState),
                                description = postActionState.description ?: post.description
                            )
                        }

                        var isSeeking by remember(post.id) { mutableStateOf(false) }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = { viewModel.togglePlayPause(detailScopeKey, page) }
                                )
                        ) {
                            if (player != null) {
                                PostPlayerWithThumbnail(
                                    player = player!!,
                                    showPlayIcon = userPausedSet.contains(post.id),
                                    thumbnailUrl = post.mediaFiles.first().thumbnailUrl
                                )
                            } else {
                                AsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = post.mediaFiles.first().thumbnailUrl,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                            }

                            androidx.compose.animation.AnimatedVisibility(
                                visible = !isSeeking,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                PostOverlay(
                                    post = postUi,
                                    isSavingLike = postActionState.isSavingLike,
                                    isSavingBookmark = postActionState.isSavingBookmark,
                                    onAction = { action ->
                                        handlePostSheetAction(action, post, ::handleOpenSheet)
                                    },
                                    onLike = { viewModel.toggleLike(post) },
                                    onBookmark = { viewModel.toggleBookmark(post) },
                                    onShare = {
                                        sharePost(context, post) { channel ->
                                            viewModel.sharePost(post, channel)
                                        }
                                    },
                                    onNavigateToUserProfile = onNavigateToUserProfile,
                                    onNavigateToReviews = { onBack() },
                                    showBookButton = false,
                                )
                            }

                            if (player != null) {
                                VideoScrubber(
                                    player = player!!,
                                    isFocused = page == pagerState.settledPage,
                                    isPaused = userPausedSet.contains(post.id),
                                    onSeekingChanged = { isSeeking = it },
                                    modifier = Modifier.align(Alignment.BottomCenter)
                                )
                            }
                        }
                    }
                }

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
                                handleOpenSheet = ::handleOpenSheet
                            )
                        }
                    },
                    title = stringResource(R.string.bookNow),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewsDetailSkeleton(
    onBack: () -> Unit,
) {
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
            Box(modifier = Modifier.weight(1f)) {
                LoadingScreen(color = Color.White)
            }

            MainButton(
                modifier = Modifier.padding(
                    vertical = SpacingS,
                    horizontal = BasePadding
                ),
                contentPadding = PaddingValues(12.dp),
                enabled = false,
                onClick = {},
                title = stringResource(R.string.bookNow),
            )
        }
    }
}

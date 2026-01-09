package com.example.scrollbooker.ui.feed
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.entity.social.post.data.mappers.applyUiState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.feed.drawer.FeedDrawer
import com.example.scrollbooker.ui.profile.PostPlayerWithThumbnail
import com.example.scrollbooker.ui.shared.posts.components.PostBottomBar
import com.example.scrollbooker.ui.shared.posts.components.PostShimmer
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostControls
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlay
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheets
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.BookingsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.LocationSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.MoreOptionsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.None
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.PhoneSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.ReviewDetailsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.ReviewsSheet
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    feedViewModel: FeedScreenViewModel,
    feedNavigate: FeedNavigator,
    explorePosts: LazyPagingItems<Post>
) {
    val followingPosts = feedViewModel.followingPosts.collectAsLazyPagingItems()
    val businessDomainsState by feedViewModel.businessDomainsWithBusinessTypes.collectAsStateWithLifecycle()
    val selectedFromVm by feedViewModel.selectedBusinessTypes.collectAsStateWithLifecycle()
    val showBottomBar by feedViewModel.showBottomBar.collectAsStateWithLifecycle()
    val userPausedSet by feedViewModel.userPausedPostIds.collectAsStateWithLifecycle()

    val horizontalPagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    val currentTab by remember { derivedStateOf { horizontalPagerState.currentPage } }
    val currentPost by feedViewModel.currentPost(currentTab).collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(None) }

    var isDrawerOpen by rememberSaveable { mutableStateOf(false) }

    if(sheetState.isVisible) {
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
            )
        }
    }

    fun handleOpenSheet(targetSheet: PostSheetsContent) {
        scope.launch {
            sheetState.show()
            sheetContent = targetSheet
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        CustomDrawer(
            isOpen = isDrawerOpen,
            onOpenChange = { isDrawerOpen = it },
            drawerWidthFraction = 0.8f,
            scrimColor = BackgroundDark.copy(alpha = 0.7f)
        ) {
            FeedDrawer(
                viewModel = feedViewModel,
                isDrawerOpen = isDrawerOpen,
                businessDomainsState = businessDomainsState,
                selectedFromVm = selectedFromVm,
                onClose = { isDrawerOpen = false },
            )
        }

        FeedTabs(
            modifier = Modifier.statusBarsPadding(),
            selectedTabIndex = horizontalPagerState.settledPage,
            activeFiltersCount = selectedFromVm.size,
            onChangeTab = {
                feedViewModel.pauseAllNow()
                scope.launch { horizontalPagerState.animateScrollToPage(it) }
            },
            onOpenDrawer = { isDrawerOpen = true },
            onNavigateSearch = { feedNavigate.toFeedSearch() }
        )

        HorizontalPager(
            state = horizontalPagerState,
            overscrollEffect = null,
            beyondViewportPageCount = 0,
            userScrollEnabled = false
        ) { tabIndex ->
            val posts = if (tabIndex == 0) explorePosts else followingPosts
            val pagerState = rememberPagerState { posts.itemCount }

            val settledPage by remember { derivedStateOf { pagerState.settledPage } }

            LaunchedEffect(pagerState.settledPage) {
                snapshotFlow {
                    val page = settledPage
                    settledPage to posts.getOrNull(page)?.id
                }
                    .distinctUntilChanged()
                    .collectLatest { (page, postId) ->
                        if (postId == null) return@collectLatest

                        feedViewModel.onPageSettled(page)
                        feedViewModel.ensureWindow(
                            centerIndex = page,
                            getPost = { idx -> posts.getOrNull(idx) }
                        )
                    }
            }

            LaunchedEffect(pagerState) {
                snapshotFlow { settledPage to isDrawerOpen }
                    .distinctUntilChanged()
                    .collectLatest { (page, drawerOpen) ->
                        if(drawerOpen) feedViewModel.pauseIfPlaying(page)
                        else feedViewModel.resumeAfterDrawer(page)
                    }
            }

            val decay = rememberSplineBasedDecay<Float>()

            val snapSpec: SpringSpec<Float> = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessHigh,
            )

            val fling = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(1),
                decayAnimationSpec = decay,
                snapAnimationSpec = snapSpec
            )

            val currentOnReleasePlayer by rememberUpdatedState(feedViewModel::stopDetailSession)

            LifecycleStartEffect(true) {
                onStopOrDispose {
                    currentOnReleasePlayer()
                }
            }

            Column(Modifier.fillMaxSize()) {
                Column(Modifier.weight(1f)) {
                    when(posts.loadState.refresh) {
                        is LoadState.Error -> ErrorScreen()
                        is LoadState.Loading -> PostShimmer()
                        is LoadState.NotLoading -> {
                            VerticalPager(
                                state = pagerState,
                                overscrollEffect = null,
                                flingBehavior = fling,
                                pageSize = PageSize.Fill,
                                pageSpacing = 0.dp,
                                beyondViewportPageCount = 1,
                                modifier = Modifier.fillMaxSize(),
                            ) { page ->
                                val post = posts.getOrNull(page) ?: return@VerticalPager
                                val postId = post.id

                                key(postId) {
                                    val postActionState by feedViewModel
                                        .observePostUi(postId)
                                        .collectAsStateWithLifecycle()

                                    val postUi = remember(post, postActionState) {
                                        post.copy(
                                            userActions = post.userActions.applyUiState(postActionState),
                                            counters = post.counters.applyUiState(postActionState)
                                        )
                                    }

                                    val player = feedViewModel.getPlayerForIndex(page)

                                    Box(modifier = Modifier
                                        .fillMaxSize()
                                        .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            onClick = { feedViewModel.togglePlayer(page) }
                                        )
                                    ) {
                                        if (player != null) {
                                            PostPlayerWithThumbnail(
                                                player = player,
                                                thumbnailUrl = post.mediaFiles.first().thumbnailUrl,
                                                showPlayIcon = userPausedSet.contains(postId)
                                            )
                                        } else {
                                            AsyncImage(
                                                modifier = Modifier.fillMaxSize(),
                                                model = post.mediaFiles.first().thumbnailUrl,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop
                                            )
                                        }

                                        PostOverlay(
                                            post = postUi,
                                            postActionState = postActionState,
                                            onAction = {
                                                handlePostAction(
                                                    feedViewModel = feedViewModel,
                                                    action = it,
                                                    handleOpenSheet = { handleOpenSheet(it) },
                                                    post = post
                                                )
                                            },
                                            enableOpacity = false,
                                            showBottomBar = showBottomBar,
                                            onShowBottomBar = { feedViewModel.toggleBottomBar() },
                                            onNavigateToUserProfile = { feedNavigate.toUserProfile(it) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Column(modifier = Modifier
                    .height(90.dp)
                    .zIndex(14f)
                ) {
                    PostBottomBar(
                        onAction = { action ->
                            currentPost?.let { post ->
                                handlePostAction(
                                    feedViewModel = feedViewModel,
                                    action = action,
                                    handleOpenSheet = { handleOpenSheet(it) },
                                    post = post
                                )
                            }
                        },
                        showBottomBar = showBottomBar,
                        currentPost = currentPost
                    )
                }
            }
        }
    }
}

@Composable
fun CustomDrawer(
    isOpen: Boolean,
    onOpenChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    drawerWidthFraction: Float = 0.9f,
    scrimColor: Color = Color.Black.copy(alpha = 0.7f),
    drawerContent: @Composable ColumnScope.() -> Unit,
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .zIndex(15f)
    ) {
        val fullWidthPx = with(density) { maxWidth.toPx() }
        val drawerWidthPx = fullWidthPx * drawerWidthFraction
        val closedOffsetX = -drawerWidthPx

        val offsetX = remember { Animatable(if (isOpen) 0f else closedOffsetX) }

        LaunchedEffect(isOpen, drawerWidthPx) {
            val target = if (isOpen) 0f else closedOffsetX
            if (offsetX.targetValue != target) {
                offsetX.animateTo(target)
            }
        }

        val openProgress = ((offsetX.value - closedOffsetX) / (0f - closedOffsetX)).coerceIn(0f, 1f)
        
        if (openProgress > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(9f)
                    .graphicsLayer { alpha = openProgress }
                    .background(scrimColor)
                    .pointerInput(Unit) {
                        detectTapGestures { onOpenChange(false) }
                    }
            )
        }

        Box(
            modifier = Modifier
                .zIndex(10f)
                .fillMaxHeight()
                .width(with(density) { drawerWidthPx.toDp() })
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            val newValue = (offsetX.value + delta).coerceIn(closedOffsetX, 0f)
                            offsetX.snapTo(newValue)
                        }
                    },
                    onDragStopped = { velocity ->
                        val shouldOpen = when {
                            velocity > 800f -> true
                            velocity < -800f -> false
                            else -> openProgress > 0.5f
                        }

                        onOpenChange(shouldOpen)
                    }
                )
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
                .safeDrawingPadding()
            ) {
                drawerContent()
            }
        }
    }
}

private fun handlePostAction(
    feedViewModel: FeedScreenViewModel,
    action: PostOverlayActionEnum,
    handleOpenSheet: (PostSheetsContent) -> Unit,
    post: Post
) {
    when(action) {
        PostOverlayActionEnum.OPEN_BOOKINGS -> handleOpenSheet(BookingsSheet(post.user))
        PostOverlayActionEnum.OPEN_REVIEW_DETAILS -> handleOpenSheet(ReviewDetailsSheet(post.user.id))
        PostOverlayActionEnum.OPEN_REVIEWS -> {
            val id = if(post.isVideoReview) post.businessOwner.id else post.user.id
            handleOpenSheet(ReviewsSheet(id))
        }
        PostOverlayActionEnum.OPEN_COMMENTS -> handleOpenSheet(CommentsSheet(post.id))
        PostOverlayActionEnum.OPEN_LOCATION -> handleOpenSheet(LocationSheet(post.businessId))
        PostOverlayActionEnum.OPEN_MORE_OPTIONS -> handleOpenSheet(MoreOptionsSheet(post.user.id))
        PostOverlayActionEnum.OPEN_PHONE -> handleOpenSheet(PhoneSheet(0.7f))
        PostOverlayActionEnum.LIKE -> feedViewModel.toggleLike(post)
        PostOverlayActionEnum.BOOKMARK -> feedViewModel.toggleBookmark(post)
    }
}


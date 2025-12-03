package com.example.scrollbooker.ui.feed
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.core.extensions.getOrNull
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.feed.drawer.FeedDrawer
import com.example.scrollbooker.ui.shared.posts.PostScreen
import com.example.scrollbooker.ui.shared.posts.PostView
import com.example.scrollbooker.ui.shared.posts.components.PostBottomBar
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheets
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.BookingsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.LocationSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.MoreOptionsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.None
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.ReviewDetailsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.ReviewsSheet
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(feedNavigate: FeedNavigator) {
    val feedViewModel: FeedScreenViewModel = hiltViewModel()

    val explorePosts = feedViewModel.explorePosts.collectAsLazyPagingItems()
    val followingPosts = feedViewModel.followingPosts.collectAsLazyPagingItems()
    val businessDomainsState by feedViewModel.businessDomainsWithBusinessTypes.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val horizontalPagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    val currentTab by remember { derivedStateOf { horizontalPagerState.currentPage } }
    val currentPost by feedViewModel.currentPost(currentTab).collectAsStateWithLifecycle()
    val showBottomBar by feedViewModel.showBottomBar.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(None) }

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

    LaunchedEffect(drawerState.currentValue) {
        snapshotFlow { drawerState.currentValue }
            .collectLatest { drawerValue ->
                currentPost?.id?.let {
                    if (drawerValue == DrawerValue.Open) {
                        feedViewModel.pauseIfPlaying(it)
                    } else {
                        feedViewModel.resumeIfPlaying(it)
                    }
                }
            }
    }

    ModalNavigationDrawer(
        drawerContent = {
            FeedDrawer(
                viewModel = feedViewModel,
                businessDomainsState = businessDomainsState,
                onClose = { scope.launch { drawerState.close() } }
            )
        },
        scrimColor = BackgroundDark.copy(0.7f),
        drawerState = drawerState,
        gesturesEnabled = drawerState.currentValue == DrawerValue.Open,
    ) {
        Scaffold(
            containerColor = BackgroundDark,
            bottomBar = {
                PostBottomBar(
                    onAction = { action -> currentPost?.let { post ->
                        handlePostAction(
                            feedViewModel = feedViewModel,
                            action = action,
                            handleOpenSheet = { handleOpenSheet(it) },
                            post = post
                        )
                    } },
                    showBottomBar = showBottomBar,
                    currentPost = currentPost
                )
            }
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDark)
            ) {
                FeedTabs(
                    selectedTabIndex = horizontalPagerState.currentPage,
                    onChangeTab = { scope.launch { horizontalPagerState.animateScrollToPage(it) } },
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onNavigateSearch = { feedNavigate.toFeedSearch() }
                )

                HorizontalPager(
                    state = horizontalPagerState,
                    overscrollEffect = null,
                    beyondViewportPageCount = 0,
                    userScrollEnabled = false
                ) { tabIndex ->
                    val posts = if(tabIndex == 0) explorePosts else followingPosts

                    val pagerState = rememberPagerState { posts.itemCount }
                    val currentOnReleasePlayer by rememberUpdatedState(feedViewModel::releasePlayer)

                    LifecycleStartEffect(true) {
                        onStopOrDispose {
                            currentOnReleasePlayer(posts.getOrNull(pagerState.currentPage)?.id)
                        }
                    }

                    VerticalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 90.dp),
                        overscrollEffect = null,
                        pageSize = PageSize.Fill,
                        pageSpacing = 0.dp,
                        beyondViewportPageCount = 1
                    ) { page ->
                        posts[page]?.let { post ->
                            LaunchedEffect(pagerState) {
                                snapshotFlow { pagerState.settledPage }
                                    .collectLatest { page ->
                                        val post = posts.getOrNull(page)
                                        val previousPost = posts.getOrNull(page - 1)
                                        val nextPost = posts.getOrNull(page + 1)

                                        post?.let {
                                            feedViewModel.initializePlayer(
                                                post = post,
                                                previousPost = previousPost,
                                                nextPost = nextPost
                                            )
                                        }
                                        feedViewModel.updateCurrentPost(page, post)
                                    }
                            }

                            key(post.id) {
                                val postActionState by feedViewModel
                                    .observePostUi(post.id)
                                    .collectAsStateWithLifecycle()

                                PostView(
                                    postActionState = postActionState,
                                    viewModel = feedViewModel,
                                    post = post,
                                    onAction = { action, post ->
                                        handlePostAction(
                                            feedViewModel = feedViewModel,
                                            action = action,
                                            handleOpenSheet = { handleOpenSheet(it) },
                                            post = post
                                        )
                                    },
                                    feedNavigate = feedNavigate,
                                    isDrawerOpen = false,
                                    showBottomBar = showBottomBar,
                                    onShowBottomBar = { feedViewModel.toggleBottomBar() }
                                )
                            }
                        }
                    }
                }
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
        PostOverlayActionEnum.OPEN_BOOKINGS -> {
            handleOpenSheet(BookingsSheet(post.user))
        }
        PostOverlayActionEnum.OPEN_REVIEW_DETAILS -> {
            handleOpenSheet(ReviewDetailsSheet(post.user.id))
        }
        PostOverlayActionEnum.LIKE -> {
            feedViewModel.toggleLike(post)
        }
        PostOverlayActionEnum.BOOKMARK -> {
            feedViewModel.toggleBookmark(post)
        }
        PostOverlayActionEnum.OPEN_REVIEWS -> {
            val id = if(post.isVideoReview) post.businessOwner.id else post.user.id
            handleOpenSheet(ReviewsSheet(id))
        }
        PostOverlayActionEnum.OPEN_COMMENTS -> {
            handleOpenSheet(CommentsSheet(post.id))
        }
        PostOverlayActionEnum.OPEN_LOCATION -> {
            handleOpenSheet(LocationSheet(post.businessId))
        }
        PostOverlayActionEnum.OPEN_MORE_OPTIONS -> {
            handleOpenSheet(MoreOptionsSheet(post.user.id))
        }
    }
}


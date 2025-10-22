package com.example.scrollbooker.ui.feed
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.feed.drawer.FeedDrawer
import com.example.scrollbooker.ui.shared.posts.PostScreen
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(feedNavigate: FeedNavigator) {
    val feedViewModel: FeedScreenViewModel = hiltViewModel()

    val explorePosts = feedViewModel.explorePosts.collectAsLazyPagingItems()
    val businessDomainsState by feedViewModel.businessDomainsWithBusinessTypes.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val horizontalPagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    var shouldDisplayBottomBar by rememberSaveable { mutableStateOf(true) }

    val currentTab by remember { derivedStateOf { horizontalPagerState.currentPage } }
    val currentPostUi by feedViewModel.currentPostFor(currentTab).collectAsStateWithLifecycle()

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

    fun handleDrawerFilter() {
        scope.launch {
            feedViewModel.updateBusinessTypes()
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            FeedDrawer(
                viewModel = feedViewModel,
                businessDomainsState = businessDomainsState,
                onFilter = { handleDrawerFilter() }
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
                    onAction = { action ->
                        when(action) {
                            PostOverlayActionEnum.OPEN_PRODUCTS -> {
                                currentPostUi?.userId?.let { id ->
                                    handleOpenSheet(BookingsSheet(id))
                                }
                            }
                            PostOverlayActionEnum.OPEN_REVIEW_DETAILS -> {
                                currentPostUi?.userId?.let { id ->
                                    handleOpenSheet(ReviewDetailsSheet(id))
                                }
                            }
                            else -> Unit
                        }
                    },
                    shouldDisplayBottomBar = shouldDisplayBottomBar,
                    currentPostUi = currentPostUi
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
                    beyondViewportPageCount = 0
                ) { tabIndex ->
                    val posts = if(tabIndex == 0) explorePosts
                                else feedViewModel.followingPosts.collectAsLazyPagingItems()

                    PostScreen(
                        tabIndex = tabIndex,
                        onAction = { action, post -> handlePostAction(
                            feedViewModel = feedViewModel,
                            action = action,
                            handleOpenSheet = { handleOpenSheet(it) },
                            post = post
                        ) },
                        feedViewModel = feedViewModel,
                        posts = posts,
                        drawerState = drawerState,
                        shouldDisplayBottomBar = shouldDisplayBottomBar,
                        onShowBottomBar = { shouldDisplayBottomBar = !shouldDisplayBottomBar },
                        feedNavigate = feedNavigate
                    )
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
        PostOverlayActionEnum.OPEN_PRODUCTS -> {
            handleOpenSheet(BookingsSheet(post.user.id))
        }
        PostOverlayActionEnum.OPEN_REVIEW_DETAILS -> {
            handleOpenSheet(ReviewDetailsSheet(post.user.id))
        }
        PostOverlayActionEnum.OPEN_MORE_OPTIONS -> {
            handleOpenSheet(MoreOptionsSheet(post.user.id))
        }
    }
}


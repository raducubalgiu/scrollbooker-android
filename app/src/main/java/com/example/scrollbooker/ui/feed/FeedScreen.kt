package com.example.scrollbooker.ui.feed
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.feed.drawer.FeedDrawer
import com.example.scrollbooker.ui.feed.drawer.FeedDrawerLayout
import com.example.scrollbooker.ui.shared.posts.PostVerticalPager
import com.example.scrollbooker.ui.shared.posts.components.PostBottomBar
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheets
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.BookingsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.LocationSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.MoreOptionsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.None
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.PhoneSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.ReviewsSheet
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    feedViewModel: FeedScreenViewModel,
    feedNavigate: FeedNavigator,
) {
    val explorePosts = feedViewModel.explorePosts.collectAsLazyPagingItems()
    val followingPosts = feedViewModel.followingPosts.collectAsLazyPagingItems()

    val businessDomainsState by feedViewModel.businessDomainsWithBusinessTypes.collectAsStateWithLifecycle()
    val selectedFromVm by feedViewModel.selectedBusinessTypes.collectAsStateWithLifecycle()
    val showBottomBar by feedViewModel.showBottomBar.collectAsStateWithLifecycle()

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
        FeedDrawerLayout(
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

        Column(Modifier.fillMaxSize()) {
            Box(Modifier.weight(1f)) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = horizontalPagerState,
                    overscrollEffect = null,
                    beyondViewportPageCount = 0,
                    userScrollEnabled = false
                ) { tabIndex ->
                    val posts = if (tabIndex == 0) explorePosts else followingPosts

                    Box(Modifier.fillMaxSize()) {
                        PostVerticalPager(
                            feedViewModel = feedViewModel,
                            posts = posts,
                            isDrawerOpen = isDrawerOpen,
                            onAction = { action, post ->
                                handlePostAction(
                                    feedViewModel = feedViewModel,
                                    action = action,
                                    handleOpenSheet = { handleOpenSheet(it) },
                                    post = post
                                )
                            },
                            showBottomBar = showBottomBar,
                            onNavigateToUserProfile = { feedNavigate.toUserProfile(it) }
                        )
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

private fun handlePostAction(
    feedViewModel: FeedScreenViewModel,
    action: PostOverlayActionEnum,
    handleOpenSheet: (PostSheetsContent) -> Unit,
    post: Post
) {
    when(action) {
        PostOverlayActionEnum.OPEN_BOOKINGS -> handleOpenSheet(BookingsSheet(post.user))
        PostOverlayActionEnum.OPEN_REVIEWS -> {
            val id = if(post.isVideoReview) post.businessOwner.id else post.user.id
            handleOpenSheet(ReviewsSheet(id))
        }
        PostOverlayActionEnum.OPEN_COMMENTS -> handleOpenSheet(CommentsSheet(post.id))
        PostOverlayActionEnum.OPEN_LOCATION -> handleOpenSheet(LocationSheet(post.businessId))
        PostOverlayActionEnum.OPEN_MORE_OPTIONS -> handleOpenSheet(MoreOptionsSheet(post.user.id, post.isOwnPost))
        PostOverlayActionEnum.OPEN_PHONE -> handleOpenSheet(PhoneSheet(0.7f))
        PostOverlayActionEnum.LIKE -> feedViewModel.toggleLike(post)
        PostOverlayActionEnum.BOOKMARK -> feedViewModel.toggleBookmark(post)
    }
}


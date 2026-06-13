package com.example.scrollbooker.ui.feed
import BottomBar
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
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.feed.drawer.FeedDrawer
import com.example.scrollbooker.ui.feed.drawer.FeedDrawerLayout
import com.example.scrollbooker.ui.feed.tabs.ExploreTab
import com.example.scrollbooker.ui.feed.tabs.FollowingTab
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheets
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.CommentsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.LinkedProductsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.MoreOptionsSheet
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.None
import com.example.scrollbooker.ui.shared.posts.sheets.PostSheetsContent.ReviewsSheet
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    exploreViewModel: ExploreFeedViewModel,
    feedNavigate: FeedNavigator,
) {
    val explorePosts = exploreViewModel.explorePosts.collectAsLazyPagingItems()

    val horizontalPagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetContent by remember { mutableStateOf<PostSheetsContent>(None) }
    var isDrawerOpen by rememberSaveable { mutableStateOf(false) }

    if(sheetContent != None) {
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
            scrimColor = BackgroundDark.copy(alpha = 0.7f)
        ) {
            FeedDrawer(
                isDrawerOpen = isDrawerOpen,
                onClose = { isDrawerOpen = false },
            )
        }

        FeedTabs(
            modifier = Modifier.statusBarsPadding(),
            selectedTabIndex = horizontalPagerState.currentPage,
            onChangeTab = {
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
                    when(tabIndex) {
                        0 -> ExploreTab(
                            exploreViewModel = exploreViewModel,
                            posts = explorePosts,
                            isDrawerOpen = isDrawerOpen,
                            isTabActive = horizontalPagerState.settledPage == 0,
                            onAction = { action, post ->
                                handleSheetAction(action, post, ::handleOpenSheet)
                            },
                            onNavigateToUserProfile = { userId, username ->
                                feedNavigate.toUserProfile(userId, username)
                            },
                            onNavigateToBooking = { feedNavigate.toBooking(it) }
                        )
                        1 -> FollowingTab(
                            isDrawerOpen = isDrawerOpen,
                            isTabActive = horizontalPagerState.settledPage == 1,
                            onAction = { action, post ->
                                handleSheetAction(action, post, ::handleOpenSheet)
                            },
                            onNavigateToUserProfile = { userId, username ->
                                feedNavigate.toUserProfile(userId, username)
                            },
                            onNavigateToBooking = { feedNavigate.toBooking(it) }
                        )
                    }
                }
            }

            Column(modifier = Modifier
                .height(90.dp)
                .zIndex(14f)
            ) {
                BottomBar()
            }
        }
    }
}

private fun handleSheetAction(
    action: PostOverlayActionEnum,
    post: Post,
    handleOpenSheet: (PostSheetsContent) -> Unit
) {
    when(action) {
        PostOverlayActionEnum.OPEN_LINKED_PRODUCTS -> handleOpenSheet(LinkedProductsSheet(post.id))
        PostOverlayActionEnum.OPEN_COMMENTS -> handleOpenSheet(CommentsSheet(post.id))
        PostOverlayActionEnum.OPEN_MORE_OPTIONS -> handleOpenSheet(MoreOptionsSheet(post.user.id, post.isOwnPost))
        PostOverlayActionEnum.OPEN_REVIEWS -> {
            val id = if(post.isVideoReview) post.businessOwner.id else post.user.id
            handleOpenSheet(ReviewsSheet(id))
        }
    }
}


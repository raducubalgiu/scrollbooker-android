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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.customized.post.handlePostSheetAction
import com.example.scrollbooker.components.customized.post.sheets.PostSheets
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent.None
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.feed.drawer.FeedDrawer
import com.example.scrollbooker.ui.feed.drawer.FeedDrawerLayout
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    exploreViewModel: ExploreFeedViewModel,
    followingViewModel: FollowingFeedViewModel,
    feedNavigate: FeedNavigator,
) {
    val explorePosts = exploreViewModel.posts.collectAsLazyPagingItems()
    val isFollowingTabOpened = rememberSaveable { mutableStateOf(false) }

    val horizontalPagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    LaunchedEffect(horizontalPagerState.settledPage) {
        if (horizontalPagerState.settledPage == 1) {
            isFollowingTabOpened.value = true
        }
    }

    val followingPosts = remember(isFollowingTabOpened.value) {
        if (isFollowingTabOpened.value) followingViewModel.posts else flowOf(PagingData.empty())
    }.collectAsLazyPagingItems()

    val tabConfigs = remember(explorePosts, followingPosts) {
        listOf(
            FeedTabConfig(explorePosts, exploreViewModel, BookingSourceEnum.EXPLORE_FEED),
            FeedTabConfig(followingPosts, followingViewModel, BookingSourceEnum.FOLLOWING_FEED)
        )
    }

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

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundDark)
    ) {
        FeedDrawerLayout(
            isOpen = isDrawerOpen,
            onOpenChange = { isDrawerOpen = it },
            scrimColor = BackgroundDark.copy(alpha = 0.7f)
        ) {
            FeedDrawer(isDrawerOpen = isDrawerOpen, onClose = { isDrawerOpen = false })
        }

        FeedTabs(
            modifier = Modifier.statusBarsPadding(),
            selectedTabIndex = horizontalPagerState.currentPage,
            onChangeTab = { scope.launch { horizontalPagerState.scrollToPage(it) } },
            onOpenDrawer = { isDrawerOpen = true },
            onNavigateSearch = { feedNavigate.toFeedSearch() }
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    state = horizontalPagerState,
                    userScrollEnabled = false,
                    key = { it }
                ) { tabIndex ->
                    val config = tabConfigs[tabIndex]

                    BaseFeedTabScreen(
                        posts = config.posts,
                        isTabActive = horizontalPagerState.settledPage == tabIndex,
                        viewModel = config.viewModel,
                        onAction = { action, post ->
                            handlePostSheetAction(action, post, ::handleOpenSheet)
                        },
                        onNavigateToUserProfile = { userId, username ->
                            feedNavigate.toUserProfile(userId, username)
                        },
                        onNavigateToBooking = { post ->
                            feedNavigate.toBookingFromPost(post, config.bookingSource)
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .height(90.dp)
                    .zIndex(14f)
            ) {
                BottomBar()
            }
        }
    }
}

private data class FeedTabConfig(
    val posts: LazyPagingItems<Post>,
    val viewModel: FeedViewModelContract,
    val bookingSource: BookingSourceEnum
)


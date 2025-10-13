package com.example.scrollbooker.ui.feed
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.MainDrawer
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.feed.tabs.ExplorePostsTab
import com.example.scrollbooker.ui.feed.tabs.FollowingPostsTab
import com.example.scrollbooker.ui.modules.posts.components.PostBottomBar
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    appointmentsNumber: Int,
    notificationsNumber: Int,
    feedNavigate: FeedNavigator
) {
    val feedViewModel: FeedScreenViewModel = hiltViewModel()
    val explorePosts = feedViewModel.explorePosts.collectAsLazyPagingItems()
    val followingPosts = feedViewModel.followingPosts.collectAsLazyPagingItems()
    val businessDomainsState by feedViewModel.businessDomainsState.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val horizontalPagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    var shouldDisplayBottomBar by rememberSaveable { mutableStateOf(true) }

    fun navigateToCalendar() {
//        val userId = currentPost?.user?.id
//        val slotDuration = currentPost?.product?.duration
//        val productId = currentPost?.product?.id
//        val productName = currentPost?.product?.name
//
//        if(userId != null && slotDuration != null && productId != null && productName != null) {
//            feedNavigate.toCalendar(
//                NavigateCalendarParam(userId, slotDuration, productId, productName)
//            )
//        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            MainDrawer(
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
                    onAction = { //navigateToCalendar()
                    },
                    shouldDisplayBottomBar = shouldDisplayBottomBar,
                    appointmentsNumber = appointmentsNumber,
                    notificationsNumber = notificationsNumber,
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                FeedTabs(
                    selectedTabIndex = horizontalPagerState.currentPage,
                    onChangeTab = { scope.launch { horizontalPagerState.animateScrollToPage(it) } },
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onNavigateSearch = { feedNavigate.toFeedSearch() },
                    onNavigateToCamera = { feedNavigate.toCamera() }
                )

                HorizontalPager(
                    state = horizontalPagerState,
                    overscrollEffect = null,
                    beyondViewportPageCount = 0
                ) { page ->
                    when(page) {
                        0 -> {
                            ExplorePostsTab(
                                posts = explorePosts,
                                drawerState = drawerState,
                                shouldDisplayBottomBar = shouldDisplayBottomBar,
                                onShowBottomBar = { shouldDisplayBottomBar = !shouldDisplayBottomBar },
                                feedNavigate = feedNavigate
                            )
                        }
                        1 -> {
                            FollowingPostsTab(
                                posts = followingPosts,
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
    }
}


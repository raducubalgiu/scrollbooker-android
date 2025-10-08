@file:kotlin.OptIn(FlowPreview::class)

package com.example.scrollbooker.ui.feed
import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.scrollbooker.navigation.bottomBar.MainTab
import androidx.media3.common.util.UnstableApi
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.FeedNavigator
import com.example.scrollbooker.ui.feed.components.FeedTabs
import com.example.scrollbooker.ui.feed.tabs.ExplorePostsTab
import com.example.scrollbooker.ui.modules.posts.components.PostBottomBar
import com.example.scrollbooker.ui.theme.BackgroundDark
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    feedViewModel: FeedScreenViewModel,
    posts: LazyPagingItems<Post>,
    drawerState: DrawerState,
    appointmentsNumber: Int,
    onOpenDrawer: () -> Unit,
    onChangeTab: (MainTab) -> Unit,
    feedNavigate: FeedNavigator
) {
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

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = {
            PostBottomBar(
                onAction = {
                    //navigateToCalendar()
                },
                shouldDisplayBottomBar = shouldDisplayBottomBar,
                appointmentsNumber = appointmentsNumber,
                onChangeTab = onChangeTab,
            )
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            FeedTabs(
                selectedTabIndex = horizontalPagerState.currentPage,
                onChangeTab = { scope.launch { horizontalPagerState.animateScrollToPage(it) } },
                onOpenDrawer = onOpenDrawer,
                onNavigateSearch = { feedNavigate.toFeedSearch() },
                onNavigateToCamera = { feedNavigate.toCamera() }
            )

            HorizontalPager(
                state = horizontalPagerState,
                beyondViewportPageCount = 0
            ) { page ->
                when(page) {
                    0 -> {
                        ExplorePostsTab(
                            feedViewModel = feedViewModel,
                            posts = posts,
                            drawerState = drawerState,
                            shouldDisplayBottomBar = shouldDisplayBottomBar,
                            onShowBottomBar = { shouldDisplayBottomBar = !shouldDisplayBottomBar },
                            feedNavigate = feedNavigate
                        )
                    }
                    1 -> {
                        Box(Modifier.fillMaxSize()) {
                            Text("Book Now Posts Here")
                        }
                    }
                }
            }
        }
    }
}


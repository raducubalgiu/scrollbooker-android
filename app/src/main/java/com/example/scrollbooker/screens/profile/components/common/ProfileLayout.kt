package com.example.scrollbooker.screens.profile.components.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.sheet.BottomSheet
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.profile.components.common.tab.ProfileTabRow
import com.example.scrollbooker.screens.profile.components.common.tab.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.screens.profile.components.common.tab.info.ProfileInfoTab
import com.example.scrollbooker.screens.profile.components.common.tab.posts.ProfilePostsTab
import com.example.scrollbooker.screens.profile.components.common.tab.products.ProfileProductsTab
import com.example.scrollbooker.screens.profile.components.common.tab.reposts.ProfileRepostsTab
import com.example.scrollbooker.entity.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileLayout(
    user: UserProfile,
    onNavigate: (String) -> Unit,
    userPosts: LazyPagingItems<Post>,
    userBookmarkedPosts: LazyPagingItems<Post>?,
    userReposts: LazyPagingItems<Post>?,
    actions: @Composable (() -> Unit)
) {
    var showScheduleSheet by remember { mutableStateOf(false) }
    var globalLazyListState = rememberLazyListState()

    val tabCount = 5
    val pagerState = rememberPagerState(initialPage = 0) { tabCount }

    val lazyListStates = remember {
        List(tabCount) { LazyGridState() }
    }

    BottomSheet(
        onDismiss = { showScheduleSheet = false },
        enableCloseButton = true,
        showBottomSheet = showScheduleSheet,
        showHeader = true,
        headerTitle = "Program"
    ) { UserScheduleSheet() }

    Column {
        LazyColumn(
            state = globalLazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            item {
                ProfileCounters(
                    counters =user.counters,
                    onNavigate = { onNavigate("$it/${user.id}/${user.username}") }
                )

                ProfileUserInfo(
                    user= user,
                    actions = actions,
                    onOpenScheduleSheet = { showScheduleSheet = true },
                    onNavigateToBusinessOwner = { onNavigate("${MainRoute.UserProfile.route}/${it}") }
                )
            }

            stickyHeader { ProfileTabRow(pagerState) }

            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp - 150.dp)
                ) { page ->
                    when(page) {
                        0 -> ProfilePostsTab(
                            posts = userPosts,
                            lazyListState = lazyListStates[page],
                            onNavigate = onNavigate
                        )
                        1 -> ProfileProductsTab(lazyListStates[page])
                        2 -> ProfileRepostsTab(
                            posts = userReposts,
                            lazyListState = lazyListStates[page],
                            onNavigate = onNavigate
                        )
                        3 -> ProfileBookmarksTab(
                            posts = userBookmarkedPosts,
                            lazyListState =lazyListStates[page],
                            onNavigate = onNavigate
                        )
                        4 -> ProfileInfoTab(lazyListStates[page])
                    }
                }
            }
        }
    }
}
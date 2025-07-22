package com.example.scrollbooker.screens.profile.components

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.PositionalThreshold
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.sheet.BottomSheet
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.screens.profile.components.tab.ProfileTabRow
import com.example.scrollbooker.screens.profile.components.tab.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.screens.profile.components.tab.info.ProfileInfoTab
import com.example.scrollbooker.screens.profile.components.tab.posts.ProfilePostsTab
import com.example.scrollbooker.screens.profile.components.tab.products.ProfileProductsTab
import com.example.scrollbooker.screens.profile.components.tab.reposts.ProfileRepostsTab
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.screens.profile.components.userInformation.components.ProfileCounters
import com.example.scrollbooker.screens.profile.components.userInformation.ProfileUserInfo
import com.example.scrollbooker.screens.profile.components.userInformation.components.UserScheduleSheet
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.Primary
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileLayout(
    user: UserProfile,
    onNavigate: (String) -> Unit,
    onNavigateToCalendar: (Product) -> Unit,
    actions: @Composable (() -> Unit)
) {
    var showScheduleSheet by remember { mutableStateOf(false) }
    //var globalLazyListState = rememberLazyListState()

    val tabCount = 5
    val pagerState = rememberPagerState(initialPage = 0) { tabCount }

    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    BottomSheet(
        onDismiss = { showScheduleSheet = false },
        enableCloseButton = true,
        showBottomSheet = showScheduleSheet,
        showHeader = true,
        headerTitle = "Program"
    ) { UserScheduleSheet() }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                delay(300)
                isRefreshing = false
            }
        },
//        indicator = {
//            MyCustomIndicator(
//                state = state,
//                isRefreshing = isRefreshing,
//                modifier = Modifier.align(Alignment.TopCenter)
//            )
//        },
    ) {
        Column {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)) {
                item {
                    ProfileCounters(
                        counters = user.counters,
                        onNavigate = {
                            onNavigate("$it/${user.id}/${user.username}")
                        }
                    )

                    ProfileUserInfo(
                        user = user,
                        actions = actions,
                        onOpenScheduleSheet = { showScheduleSheet = true },
                        onNavigateToBusinessOwner = { onNavigate("${MainRoute.UserProfile.route}/${it}") }
                    )
                }

                stickyHeader { ProfileTabRow(pagerState) }

                item {
                    HorizontalPager(
                        state = pagerState,
                        beyondViewportPageCount = 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(LocalConfiguration.current.screenHeightDp.dp - 150.dp)
                    ) { page ->
                        when(page) {
                            0 -> ProfilePostsTab(
                                userId = user.id,
                                onNavigate = onNavigate
                            )
                            1 -> ProfileProductsTab(
                                userId = user.id,
                                businessId = user.businessId,
                                onNavigateToCalendar = onNavigateToCalendar
                            )
                            2 -> ProfileRepostsTab(
                                userId = user.id,
                                onNavigate = onNavigate
                            )
                            3 -> ProfileBookmarksTab(
                                userId = user.id,
                                onNavigate = onNavigate
                            )
                            4 -> ProfileInfoTab()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCustomIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.pullToRefreshIndicator(
            state = state,
            isRefreshing = isRefreshing,
            containerColor = PullToRefreshDefaults.containerColor,
            threshold = PositionalThreshold
        ),
        contentAlignment = Alignment.Center
    ) {
        Crossfade(
            targetState = isRefreshing,
            animationSpec = tween(durationMillis = 250),
            modifier = Modifier.align(Alignment.Center)
        ) { refreshing ->
            if (refreshing) {
                CircularProgressIndicator(Modifier.size(20.dp))
            } else {
                val distanceFraction = { state.distanceFraction.coerceIn(0f, 1f) }
                Icon(
                    imageVector = Icons.Filled.CloudDownload,
                    contentDescription = "Refresh",
                    modifier = Modifier
                        .size(18.dp)
                        .graphicsLayer {
                            val progress = distanceFraction()
                            this.alpha = progress
                            this.scaleX = progress
                            this.scaleY = progress
                        },
                )
            }
        }
    }
}
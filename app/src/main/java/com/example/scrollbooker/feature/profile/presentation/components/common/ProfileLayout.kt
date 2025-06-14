package com.example.scrollbooker.feature.profile.presentation.components.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.sheet.BottomSheet
import com.example.scrollbooker.feature.profile.domain.model.UserProfile
import com.example.scrollbooker.feature.profile.presentation.components.common.tab.ProfileBookmarksTab
import com.example.scrollbooker.feature.profile.presentation.components.common.tab.ProfileInfoTab
import com.example.scrollbooker.feature.profile.presentation.components.common.tab.ProfilePostsTab
import com.example.scrollbooker.feature.profile.presentation.components.common.tab.ProfileProductsTab
import com.example.scrollbooker.feature.profile.presentation.components.common.tab.ProfileRepostsTab
import com.example.scrollbooker.feature.profile.presentation.components.common.tab.ProfileTab
import com.example.scrollbooker.feature.profile.presentation.components.common.tab.ProfileTabRow

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileLayout(
    user: UserProfile,
    onNavigate: (String) -> Unit,
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
                )
            }

            stickyHeader { ProfileTabRow(pagerState) }

            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp)
                ) { page ->
                    when(page) {
                        0 -> ProfilePostsTab(lazyListStates[page])
                        1 -> ProfileProductsTab(lazyListStates[page])
                        2 -> ProfileRepostsTab(lazyListStates[page])
                        3 -> ProfileBookmarksTab(lazyListStates[page])
                        4 -> ProfileInfoTab(lazyListStates[page])
                    }
                }
            }
        }
    }
}
package com.example.scrollbooker.feature.userSocial.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Tabs
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.feature.userSocial.presentation.components.UserSocialList
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue

@Composable
fun UserSocialScreen(
    viewModal: UserSocialViewModel,
    onBack: () -> Unit,
    initialPage: Int,
    username: String
) {
    val pagerState = rememberPagerState(initialPage = initialPage ) { 3 }
    val selectedTabIndex = pagerState.currentPage
    val tabs = listOf(
        stringResource(id = R.string.reviews),
        stringResource(id = R.string.followers),
        stringResource(id = R.string.following)
    )
    val coroutineScope = rememberCoroutineScope()

    Layout(
        headerTitle = username,
        onBack = onBack,
        enablePadding = false
    ) {
        Column {
            Tabs(tabs, selectedTabIndex, onChangeTab = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(it)
                }
            })

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 0,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when(page) {
                    0 -> {
                        //val customerPagingItems = viewModel.customerAppointments().collectAsLazyPagingItems()
                        Column(Modifier.fillMaxSize()) {
                            Text("Reviews Screen")
                        }
                    }
                    1 -> {
                        val userFollowers = viewModal.userFollowers.collectAsLazyPagingItems()
                        val followedOverrides by viewModal.followedOverrides.collectAsState()
                        val followRequestLocks by viewModal.followRequestLocks.collectAsState()
                        UserSocialList(
                            pagingItems = userFollowers,
                            followedOverrides = followedOverrides,
                            followRequestLocks = followRequestLocks,
                            onFollow = { isFollowed, userId ->
                                viewModal.onFollow(isFollowed, userId)
                            }
                        )
                    }

                    2 -> {
                        val userFollowings = viewModal.userFollowings.collectAsLazyPagingItems()
                        val followedOverrides by viewModal.followedOverrides.collectAsState()
                        val followRequestLocks by viewModal.followRequestLocks.collectAsState()
                        UserSocialList(
                            pagingItems = userFollowings,
                            followedOverrides = followedOverrides,
                            followRequestLocks = followRequestLocks,
                            onFollow = { isFollowed, userId ->
                                viewModal.onFollow(isFollowed, userId)
                            }
                        )
                    }
                }
            }
        }
    }
}
package com.example.scrollbooker.screens.profile.social

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.tabs.Tabs
import com.example.scrollbooker.components.core.layout.Layout
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.scrollbooker.screens.profile.social.components.ReviewsList
import com.example.scrollbooker.screens.profile.social.components.UserSocialList

@Composable
fun UserSocialScreen(
    viewModal: UserSocialViewModel,
    onBack: () -> Unit,
    initialPage: Int,
    username: String,
    onNavigateUserProfile: (Int) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = initialPage ) { 3 }
    val selectedTabIndex = pagerState.currentPage
    val tabs = listOf(
        stringResource(id = R.string.reviews),
        stringResource(id = R.string.followers),
        stringResource(id = R.string.following)
    )
    val coroutineScope = rememberCoroutineScope()

    var didLoadSummary by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        if(pagerState.currentPage == 0 && !didLoadSummary) {
            viewModal.loadUserReviews()
            didLoadSummary = true
        }
    }

    Layout(
        headerTitle = username,
        onBack = onBack,
        enablePaddingH = false,
        enablePaddingV = false
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
                        val pagingItems = viewModal.userReviews.collectAsLazyPagingItems()
                        val summaryState by viewModal.userReviewsSummary.collectAsState()
                        val selectedRatings by viewModal.selectedRatings

                        ReviewsList(
                            pagingItems,
                            summaryState,
                            onRatingClick = { viewModal.toggleRatingFilter(it) },
                            selectedRatings = selectedRatings
                        )
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
                            },
                            onNavigateUserProfile = onNavigateUserProfile
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
                            },
                            onNavigateUserProfile = onNavigateUserProfile
                        )
                    }
                }
            }
        }
    }
}
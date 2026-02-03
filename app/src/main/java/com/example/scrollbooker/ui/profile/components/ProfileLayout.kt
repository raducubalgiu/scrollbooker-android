package com.example.scrollbooker.ui.profile.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.rememberCollapsingNestedScroll
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.sheets.UserScheduleSheet
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileUserInfo
import com.example.scrollbooker.ui.profile.tabs.ProfileHorizontalPager
import com.example.scrollbooker.ui.profile.tabs.ProfileTab
import com.example.scrollbooker.ui.profile.tabs.ProfileTabRow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileLayout(
    layoutViewModel: ProfileLayoutViewModel,
    innerPadding: PaddingValues,
    profile: FeatureState<UserProfile>,
    profileNavigate: ProfileNavigator,
    onNavigateToPost: (SelectedPostUi) -> Unit,
    onNavigateToSocial: (NavigateSocialParam) -> Unit,
    posts: LazyPagingItems<Post>,
    isFollow: Boolean = false,
    isFollowEnabled: Boolean = false,
    onFollow: (() -> Unit)? = null
) {
    val scope = rememberCoroutineScope()

    var headerHeightPx by remember { mutableIntStateOf(0) }
    var headerOffset by remember { mutableFloatStateOf(0f) }

    val currentTab by layoutViewModel.currentTab.collectAsStateWithLifecycle()

    val scheduleSheetState = rememberModalBottomSheetState()

    val nestedScrollConnection = rememberCollapsingNestedScroll(
        headerHeightPx = headerHeightPx,
        headerOffset = headerOffset,
        onHeaderOffsetChanged = { headerOffset = it }
    )

    if(scheduleSheetState.isVisible) {
        UserScheduleSheet(
            sheetState = scheduleSheetState,
            layoutViewModel = layoutViewModel,
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when (val profileData = profile) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> ProfileShimmer()
            is FeatureState.Success -> {
                val user = profileData.data

                val tabs = remember(user.isBusinessOrEmployee, user.isOwnProfile) {
                    ProfileTab.getTabs(user.isBusinessOrEmployee, user.isOwnProfile)
                }

                val pagerState = rememberPagerState(initialPage = currentTab) { tabs.size }

                LaunchedEffect(pagerState) {
                    snapshotFlow {
                        pagerState.currentPage
                    }
                        .distinctUntilChanged()
                        .collectLatest { page -> layoutViewModel.setCurrentTab(page) }
                }

                PullToRefreshBox(
                    isRefreshing = false,
                    onRefresh = {  }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(nestedScrollConnection)
                    ) {
                        Column(
                            modifier = Modifier
                                .graphicsLayer {
                                    translationY = headerHeightPx + headerOffset
                                }
                        ) {
                            ProfileTabRow(
                                selectedTabIndex = pagerState.currentPage,
                                onChangeTab = {
                                    layoutViewModel.setCurrentTab(it)
                                    scope.launch { pagerState.animateScrollToPage(it) }
                                },
                                tabs = tabs
                            )

                            ProfileHorizontalPager(
                                layoutViewModel = layoutViewModel,
                                pagerState = pagerState,
                                profileNavigate = profileNavigate,
                                tabs = tabs,
                                onNavigateToPost = onNavigateToPost,
                                posts = posts
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset { IntOffset(0, headerOffset.roundToInt()) }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onSizeChanged { size -> headerHeightPx = size.height }
                            ) {
                                ProfileUserInfo(
                                    user = user,
                                    isFollow = isFollow,
                                    isFollowEnabled = isFollowEnabled,
                                    onFollow = onFollow,
                                    onNavigateToSocial = onNavigateToSocial,
                                    onOpenScheduleSheet = { scope.launch { scheduleSheetState.show() } },
                                    onNavigateToBusinessOwner = { it?.let { profileNavigate.toUserProfile(it) } },
                                    onNavigateToEditProfile = { profileNavigate.toEditProfile() },
                                    onNavigateToMyCalendar = { profileNavigate.toMyCalendar() },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
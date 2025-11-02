package com.example.scrollbooker.ui.profile.components

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.NavigateSocialParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.userInfo.components.MyProfileActions
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileUserInfo
import com.example.scrollbooker.ui.profile.components.userInfo.components.ProfileCounters
import com.example.scrollbooker.ui.profile.components.sheets.UserScheduleSheet
import com.example.scrollbooker.ui.profile.components.userInfo.components.UserProfileActions
import com.example.scrollbooker.ui.profile.tabs.ProfileTab
import com.example.scrollbooker.ui.profile.tabs.ProfileTabRow
import com.example.scrollbooker.ui.profile.tabs.ProfileTabViewModel
import com.example.scrollbooker.ui.profile.tabs.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tabs.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tabs.posts.ProfilePostsTab
import com.example.scrollbooker.ui.profile.tabs.reposts.ProfileRepostsTab
import com.example.scrollbooker.ui.shared.products.UserProductsServiceTabs
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ProfileLayout(
    isInitLoading: Boolean,
    profileData: FeatureState<UserProfile>,
    isFollow: Boolean? = null,
    isFollowEnabled: Boolean,
    onFollow: (() -> Unit)? = null,
    posts: LazyPagingItems<Post>,
    profileNavigate: ProfileNavigator
) {
    val scope = rememberCoroutineScope()
    val scheduleSheetState = rememberModalBottomSheetState()
    val userId = (profileData as? FeatureState.Success<UserProfile>)?.data?.id

    if(scheduleSheetState.isVisible) {
        userId?.let { UserScheduleSheet(userId = it, sheetState = scheduleSheetState) }
    }

    val density = LocalDensity.current

    var collapsibleHeightPx by remember { mutableIntStateOf(0) }
    var tabRowHeightPx by remember { mutableIntStateOf(0) }
    var headerOffsetPx by remember { mutableIntStateOf(0) }

    val currentHeaderHeightDp = with(density) { (collapsibleHeightPx + tabRowHeightPx + headerOffsetPx).toDp() }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta < 0 && headerOffsetPx > -collapsibleHeightPx) {
                    val oldOffset = headerOffsetPx.toFloat()
                    val newOffset = (oldOffset + delta).coerceAtLeast(-collapsibleHeightPx.toFloat())
                    headerOffsetPx = newOffset.toInt()
                    return Offset(0f, newOffset - oldOffset)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                if (delta > 0 && headerOffsetPx < 0) {
                    val oldOffset = headerOffsetPx.toFloat()
                    val newOffset = (oldOffset + delta).coerceAtMost(0f)
                    headerOffsetPx = newOffset.toInt()
                    return Offset(0f, newOffset - oldOffset)
                }
                return Offset.Zero
            }
        }
    }

    if (isInitLoading) {
        ProfileShimmer()
        LoadingScreen()
    } else {
        when (val profileData = profileData) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> Unit
            is FeatureState.Success -> {
                val user = profileData.data

                val tabs = remember(user.isBusinessOrEmployee) {
                    ProfileTab.getTabs(user.isBusinessOrEmployee)
                }

                val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

                Box(modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection)
                ) {
                    // Tab Content
                    HorizontalPager(
                        state = pagerState,
                        beyondViewportPageCount = 0,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val viewModel: ProfileTabViewModel = hiltViewModel()

                        LaunchedEffect(user.id) {
                            viewModel.setUserId(userId = user.id)
                        }

                        when (tabs[page]) {
                            ProfileTab.Posts -> ProfilePostsTab(
                                paddingTop = currentHeaderHeightDp,
                                posts = posts,
                                onNavigateToPost = { profileNavigate.toPostDetail() }
                            )

                            ProfileTab.Products -> {
                                UserProductsServiceTabs(
                                    paddingTop = currentHeaderHeightDp,
                                    userId = user.id,
                                    onSelect = {}
                                )
                            }

                            ProfileTab.Reposts -> ProfileRepostsTab(
                                paddingTop = currentHeaderHeightDp,
                                viewModel = viewModel,
                                onNavigateToPost = { profileNavigate.toPostDetail() }
                            )

                            ProfileTab.Bookmarks -> ProfileBookmarksTab(
                                paddingTop = currentHeaderHeightDp,
                                viewModel = viewModel,
                                onNavigateToPost = { profileNavigate.toPostDetail() }
                            )

                            ProfileTab.Info -> ProfileInfoTab(paddingTop = currentHeaderHeightDp)
                        }
                    }

                    // Header Overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset { IntOffset(0, headerOffsetPx) }
                            .pointerInput(Unit) {
                                detectVerticalDragGestures { change, dragAmount ->
                                    val oldOffset = headerOffsetPx.toFloat()
                                    val newOffset = (oldOffset + dragAmount).coerceIn(
                                        -collapsibleHeightPx.toFloat(),
                                        0f
                                    )
                                    headerOffsetPx = newOffset.toInt()
                                    if (newOffset != oldOffset) {
                                        change.consume()
                                    }
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged { size ->
                                    collapsibleHeightPx = size.height
                                }
                        ) {
                            ProfileCounters(
                                counters = user.counters,
                                isBusinessOrEmployee = user.isBusinessOrEmployee,
                                onNavigateToSocial = { tabIndex ->
                                    profileNavigate.toSocial(
                                        NavigateSocialParam(
                                            tabIndex = tabIndex,
                                            userId = user.id,
                                            username = user.username,
                                            isBusinessOrEmployee = user.isBusinessOrEmployee
                                        )
                                    )
                                }
                            )

                            ProfileUserInfo(
                                user = user,
                                actions = {
                                    if (user.isOwnProfile) {
                                        MyProfileActions(
                                            onEditProfile = { profileNavigate.toEditProfile() },
                                            isBusinessOrEmployee = user.isBusinessOrEmployee,
                                            onNavigateToMyCalendar = { profileNavigate.toMyCalendar() }
                                        )
                                    } else {
                                        UserProfileActions(
                                            isFollow = isFollow,
                                            isFollowEnabled = isFollowEnabled,
                                            onFollow = onFollow,
                                            onNavigateToCalendar = {}
                                        )
                                    }
                                },
                                onOpenScheduleSheet = { scope.launch { scheduleSheetState.show() } },
                                onNavigateToBusinessOwner = { ownerId -> ownerId?.let {
                                        profileNavigate.toBusinessOwner(ownerId)
                                    }
                                }
                            )
                        }

                        ProfileTabRow(
                            pagerState = pagerState,
                            tabs = tabs,
                            onTabRowSizeChanged = { tabRowHeightPx = it.height },
                        )
                    }
                }
            }
        }
    }
}
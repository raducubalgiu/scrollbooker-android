package com.example.scrollbooker.ui.profile
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.profileHeader.ProfileHeader
import com.example.scrollbooker.ui.profile.components.profileHeader.ProfileShimmer
import com.example.scrollbooker.ui.profile.tabs.ProfileTab
import com.example.scrollbooker.ui.profile.tabs.ProfileTabRow
import com.example.scrollbooker.ui.profile.tabs.ProfileTabViewModel
import com.example.scrollbooker.ui.profile.tabs.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tabs.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tabs.posts.ProfilePostsTab
import com.example.scrollbooker.ui.profile.tabs.reposts.ProfileRepostsTab
import com.example.scrollbooker.ui.shared.modules.products.UserProductsServiceTabs
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    profileNavigate: ProfileNavigator
) {
    val userProfileState by viewModel.userProfileState.collectAsState()
    val posts = viewModel.userPosts.collectAsLazyPagingItems()
    val isInitLoading by viewModel.isInitLoading.collectAsState()

    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
    ) {
        if(isInitLoading) {
            ProfileShimmer()
            LoadingScreen()
        } else {
            when(val profileData = userProfileState) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> Unit
                is FeatureState.Success -> {
                    val user = profileData.data

                    val tabs = remember(user.isBusinessOrEmployee) {
                        ProfileTab.getTabs(user.isBusinessOrEmployee)
                    }

                    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

                    Column(Modifier.fillMaxSize()) {
                        Header(
                            title = user.username,
                            onBack = onBack
                        )

                        PullToRefreshBox(
                            state = state,
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                scope.launch {
                                    isRefreshing = true
                                    viewModel.loadUserProfile(user.id)
                                    isRefreshing = false
                                }
                            }
                        ) {
                            LazyColumn {
                                item {
                                    ProfileHeader(
                                        user = user,
                                        onOpenScheduleSheet = {},
                                        onNavigateToSocial = { profileNavigate.toSocial(it) },
                                        onNavigateToEditProfile = {  },
                                        onNavigateToBusinessOwner = { ownerId ->
                                            ownerId?.let {
                                                profileNavigate.toBusinessOwner(ownerId)
                                            }
                                        }
                                    )
                                }

                                stickyHeader { ProfileTabRow(pagerState, tabs) }

                                item {
                                    HorizontalPager(
                                        state = pagerState,
                                        beyondViewportPageCount = 0,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(LocalConfiguration.current.screenHeightDp.dp - 150.dp)
                                    ) { page ->
                                        val viewModel: ProfileTabViewModel = hiltViewModel()

                                        LaunchedEffect(user.id) {
                                            viewModel.setUserId(user.id)
                                        }

                                        when (tabs[page]) {
                                            ProfileTab.Posts -> ProfilePostsTab(
                                                isOwnProfile = user.isOwnProfile,
                                                posts = posts,
                                                onNavigateToPostDetail = { profileNavigate.toPostDetail() }
                                            )

                                            ProfileTab.Products -> {
                                                UserProductsServiceTabs(
                                                    userId = user.id,
                                                    onNavigateToCalendar = { profileNavigate.toCalendar(it) }
                                                )
                                            }

                                            ProfileTab.Reposts -> ProfileRepostsTab(
                                                viewModel = viewModel,
                                                isOwnProfile = user.isOwnProfile,
                                                onNavigateToPostDetail = { profileNavigate.toPostDetail() }
                                            )

                                            ProfileTab.Bookmarks -> ProfileBookmarksTab(
                                                viewModel = viewModel,
                                                isOwnProfile = user.isOwnProfile,
                                                onNavigateToPostDetail = { profileNavigate.toPostDetail() }
                                            )

                                            ProfileTab.Info -> ProfileInfoTab()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
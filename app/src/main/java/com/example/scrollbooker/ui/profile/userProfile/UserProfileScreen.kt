package com.example.scrollbooker.ui.profile.userProfile
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
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.profile.components.userInformation.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.userInformation.ProfileUserInfo
import com.example.scrollbooker.ui.profile.components.userInformation.components.ProfileCounters
import com.example.scrollbooker.ui.profile.components.userProfile.UserProfileActions
import com.example.scrollbooker.ui.profile.tab.ProfileTab
import com.example.scrollbooker.ui.profile.tab.ProfileTabRow
import com.example.scrollbooker.ui.profile.tab.ProfileTabViewModel
import com.example.scrollbooker.ui.profile.tab.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tab.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tab.posts.ProfilePostsTab
import com.example.scrollbooker.ui.profile.tab.products.ProfileProductsTab
import com.example.scrollbooker.ui.profile.tab.reposts.ProfileRepostsTab
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: ProfileViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onNavigateToCalendar: (Product) -> Unit
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
                                    ProfileCounters(
                                        counters = user.counters,
                                        isBusinessOrEmployee = user.isBusinessOrEmployee,
                                        onNavigate = {
                                            onNavigate("$it/${user.id}/${user.username}/${user.isBusinessOrEmployee}")
                                        }
                                    )

                                    ProfileUserInfo(
                                        user = user,
                                        actions = {
                                            UserProfileActions(
                                                isFollow = user.isFollow,
                                                onNavigateToCalendar = { onNavigate(MainRoute.Calendar.route) }
                                            )
                                        },
                                        onOpenScheduleSheet = {  },
                                        onNavigateToBusinessOwner = { onNavigate("${MainRoute.UserProfile.route}/${it}") }
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

                                        when(tabs[page]) {
                                            ProfileTab.Posts -> ProfilePostsTab(
                                                isOwnProfile = user.isOwnProfile,
                                                posts = posts,
                                                onNavigate = onNavigate
                                            )
                                            ProfileTab.Products -> ProfileProductsTab(
                                                userId = user.id,
                                                isOwnProfile = user.isOwnProfile,
                                                businessId = user.businessId,
                                                onNavigateToCalendar = onNavigateToCalendar
                                            )
                                            ProfileTab.Reposts -> ProfileRepostsTab(
                                                viewModel = viewModel,
                                                isOwnProfile = user.isOwnProfile,
                                                onNavigate = onNavigate
                                            )
                                            ProfileTab.Bookmarks -> ProfileBookmarksTab(
                                                viewModel = viewModel,
                                                isOwnProfile = user.isOwnProfile,
                                                onNavigate = onNavigate
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
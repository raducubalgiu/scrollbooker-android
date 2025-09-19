package com.example.scrollbooker.ui.profile
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.myProfile.MyProfileHeader
import com.example.scrollbooker.ui.profile.components.myProfile.MyProfileMenuList
import com.example.scrollbooker.ui.profile.components.profileHeader.ProfileHeader
import com.example.scrollbooker.ui.profile.components.profileHeader.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.profileHeader.components.UserScheduleSheet
import com.example.scrollbooker.ui.profile.MyProfileViewModel
import com.example.scrollbooker.ui.profile.tabs.ProfileTab
import com.example.scrollbooker.ui.profile.tabs.ProfileTabRow
import com.example.scrollbooker.ui.profile.tabs.ProfileTabViewModel
import com.example.scrollbooker.ui.profile.tabs.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tabs.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tabs.posts.ProfilePostsTab
import com.example.scrollbooker.ui.profile.tabs.reposts.ProfileRepostsTab
import com.example.scrollbooker.ui.shared.modules.products.UserProductsServiceTabs
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    viewModel: MyProfileViewModel,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit,
    profileNavigate: ProfileNavigator
) {
    val menuSheetState = rememberModalBottomSheetState()
    val scheduleSheetState = rememberModalBottomSheetState()

    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val isInitLoading by viewModel.isInitLoading.collectAsState()

    if(scheduleSheetState.isVisible) {
        Sheet(
            sheetState = scheduleSheetState,
            onClose = { scope.launch { scheduleSheetState.hide() } }
        ) {
            SheetHeader(
                title = stringResource(R.string.scheduleShort),
                onClose = { scope.launch { scheduleSheetState.hide() } }
            )
            UserScheduleSheet()
        }
    }

    if(menuSheetState.isVisible) {
        Sheet(
            sheetState = menuSheetState,
            onClose = { scope.launch { menuSheetState.hide() } }
        ) {
            MyProfileMenuList(
                onNavigateToCreatePost = {
                    scope.launch {
                        menuSheetState.hide()

                        if (!menuSheetState.isVisible) {
                            profileNavigate.toCreatePost()
                        }
                    }
                },
                onNavigateToMyBusiness = {
                    scope.launch {
                        menuSheetState.hide()

                        if (!menuSheetState.isVisible) {
                            profileNavigate.toMyBusiness()
                        }
                    }
                },
                onNavigateToSettings = {
                    scope.launch {
                        menuSheetState.hide()

                        if (!menuSheetState.isVisible) {
                            profileNavigate.toSettings()
                        }
                    }
                },
            )
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                appointmentsNumber = appointmentsNumber,
                currentTab = MainTab.Profile,
                currentRoute = MainRoute.MyProfile.route,
                onNavigate = onChangeTab
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            if (isInitLoading) {
                ProfileShimmer()
                LoadingScreen()
            } else {
                when (val profileData = myProfileData) {
                    is FeatureState.Error -> ErrorScreen()
                    is FeatureState.Loading -> Unit
                    is FeatureState.Success -> {
                        val user = profileData.data

                        val tabs = remember(user.isBusinessOrEmployee) {
                            ProfileTab.getTabs(user.isBusinessOrEmployee)
                        }

                        val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

                        Column(Modifier.fillMaxSize()) {
                            MyProfileHeader(
                                username = user.username,
                                onOpenBottomSheet = { scope.launch { menuSheetState.show() } }
                            )

                            PullToRefreshBox(
                                state = state,
                                isRefreshing = isRefreshing,
                                onRefresh = {
                                    scope.launch {
                                        isRefreshing = true
                                        viewModel.loadUserProfile()
                                        isRefreshing = false
                                    }
                                }
                            ) {
                                LazyColumn {
                                    item {
                                        ProfileHeader(
                                            user = user,
                                            onOpenScheduleSheet = {
                                                scope.launch {
                                                    scheduleSheetState.show()
                                                }
                                            },
                                            onNavigateToSocial = { profileNavigate.toSocial(it) },
                                            onNavigateToEditProfile = { profileNavigate.toEditProfile() },
                                            onNavigateToBusinessOwner = { ownerId ->
                                                ownerId?.let {
                                                    profileNavigate.toBusinessOwner(ownerId)
                                                }
                                            }
                                        )
                                    }

                                    stickyHeader {
                                        ProfileTabRow(pagerState, tabs)

                                        HorizontalPager(
                                            state = pagerState,
                                            beyondViewportPageCount = 0,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(LocalConfiguration.current.screenHeightDp.dp - 150.dp)
                                        ) { page ->
                                            val viewModel: ProfileTabViewModel = hiltViewModel()

                                            LaunchedEffect(user.id) {
                                                viewModel.setUserId(userId = user.id)
                                            }

                                            when (tabs[page]) {
                                                ProfileTab.Posts -> ProfilePostsTab(
                                                    isOwnProfile = user.isOwnProfile,
                                                    posts = myPosts,
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
}
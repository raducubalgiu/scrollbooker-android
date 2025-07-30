package com.example.scrollbooker.ui.profile.myProfile
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.sheet.BottomSheet
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.profile.components.myProfile.MyProfileActions
import com.example.scrollbooker.ui.profile.components.myProfile.MyProfileHeader
import com.example.scrollbooker.ui.profile.components.myProfile.MyProfileMenuList
import com.example.scrollbooker.ui.profile.components.userInformation.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.userInformation.ProfileUserInfo
import com.example.scrollbooker.ui.profile.components.userInformation.components.ProfileCounters
import com.example.scrollbooker.ui.profile.components.userInformation.components.UserScheduleSheet
import com.example.scrollbooker.ui.profile.tab.ProfileTab
import com.example.scrollbooker.ui.profile.tab.ProfileTabRow
import com.example.scrollbooker.ui.profile.tab.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tab.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tab.posts.ProfilePostsTab
import com.example.scrollbooker.ui.profile.tab.products.ProfileProductsTab
import com.example.scrollbooker.ui.profile.tab.reposts.ProfileRepostsTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    myProfileData: FeatureState<UserProfile>,
    viewModel: ProfileSharedViewModel,
    onNavigate: (String) -> Unit,
    onNavigateToCalendar: (Product) -> Unit,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var showScheduleSheet by remember { mutableStateOf(false) }

    val state = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    BottomSheet(
        onDismiss = { showScheduleSheet = false },
        enableCloseButton = true,
        showBottomSheet = showScheduleSheet,
        showHeader = true,
        headerTitle = stringResource(R.string.schedule)
    ) { UserScheduleSheet() }

    if(sheetState.isVisible) {
        Sheet(
            sheetState = sheetState,
            onClose = { scope.launch { sheetState.hide() } }
        ) {
            Spacer(Modifier.height(BasePadding))

            MyProfileMenuList(
                onNavigate = {
                    scope.launch {
                        sheetState.hide()

                        if(!sheetState.isVisible) {
                            onNavigate(it)
                        }
                    }
                }
            )

            Spacer(Modifier.height(SpacingXXL))
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
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when(myProfileData) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> ProfileShimmer()
                is FeatureState.Success -> {
                    val user = myProfileData.data

                    val tabs = remember(user.isBusinessOrEmployee) {
                        ProfileTab.getTabs(user.isBusinessOrEmployee)
                    }

                    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

                    Column(Modifier.fillMaxSize()) {
                        MyProfileHeader(
                            username = user.username,
                            onOpenBottomSheet = { scope.launch { sheetState.show() } }
                        )

                        PullToRefreshBox(
                            state = state,
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                scope.launch {
                                    isRefreshing = true
                                    delay(300)
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
                                            MyProfileActions(
                                                onEditProfile = { onNavigate(MainRoute.EditProfile.route) },
                                            )
                                        },
                                        onOpenScheduleSheet = { showScheduleSheet = true },
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
                                        when(tabs[page]) {
                                            ProfileTab.Posts -> {
                                                Box(Modifier.fillMaxSize())
//                                        ProfilePostsTab(
//                                            userId = user.id,
//                                            isOwnProfile = user.isOwnProfile,
//                                            onNavigate = onNavigate
//                                        )
                                            }
                                            ProfileTab.Products -> ProfileProductsTab(
                                                userId = user.id,
                                                isOwnProfile = user.isOwnProfile,
                                                businessId = user.businessId,
                                                onNavigateToCalendar = onNavigateToCalendar
                                            )
                                            ProfileTab.Reposts -> ProfileRepostsTab(
                                                userId = user.id,
                                                isOwnProfile = user.isOwnProfile,
                                                onNavigate = onNavigate
                                            )
                                            ProfileTab.Bookmarks -> ProfileBookmarksTab(
                                                userId = user.id,
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
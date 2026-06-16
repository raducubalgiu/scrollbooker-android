package com.example.scrollbooker.ui.profile
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.customized.protected.Protected
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.Dimens.IconSizeXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.rememberCollapsingNestedScroll
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.NavigateBookingParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.UserPermissionsController
import com.example.scrollbooker.ui.profile.components.sheets.ProfileMenuSheet
import com.example.scrollbooker.ui.profile.components.sheets.UserScheduleSheet
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileUserInfo
import com.example.scrollbooker.ui.profile.tabs.ProfileTab
import com.example.scrollbooker.ui.profile.tabs.ProfileTabRow
import com.example.scrollbooker.ui.profile.tabs.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tabs.employees.ProfileEmployeesTab
import com.example.scrollbooker.ui.profile.tabs.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tabs.posts.ProfilePostsTab
import com.example.scrollbooker.ui.profile.tabs.products.ProfileProductsTab
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileScreen(
    viewModel: MyProfileViewModel,
    permissionController: UserPermissionsController,
    myProfileData: FeatureState<UserProfile>,
    myPosts: LazyPagingItems<Post>,
    profileNavigate: ProfileNavigator,
) {
    val profile = (myProfileData as? FeatureState.Success)?.data

    val scope = rememberCoroutineScope()
    val menuSheetState = rememberModalBottomSheetState()

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()

    var headerHeightPx by remember { mutableIntStateOf(0) }
    var headerOffset by remember { mutableFloatStateOf(0f) }

    val scheduleSheetState = rememberModalBottomSheetState()

    val nestedScrollConnection = rememberCollapsingNestedScroll(
        headerHeightPx = headerHeightPx,
        headerOffset = headerOffset,
        onHeaderOffsetChanged = { headerOffset = it }
    )

    if(scheduleSheetState.isVisible) {
        UserScheduleSheet(
            sheetState = scheduleSheetState,
            schedulesFlow = viewModel.schedules
        )
    }

    if(menuSheetState.isVisible) {
        ProfileMenuSheet(
            sheetState = menuSheetState,
            profileNavigate = profileNavigate,
            permissionController = permissionController
        )
    }

    Scaffold(
        topBar = {
            MyProfileHeader(
                username = profile?.username ?: "",
                onNavigateToCamera = { profileNavigate.toCamera() },
                onOpenMenu = { scope.launch { menuSheetState.show() } }
            )
        },
        bottomBar = { BottomBar() },
        containerColor = Background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val profileData = myProfileData) {
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Loading -> ProfileShimmer()
                is FeatureState.Success -> {
                    val user = profileData.data
                    val isEmployee = user.isBusinessOrEmployee && user.id != user.businessOwner?.id

                    val tabs = remember(user.isBusinessOrEmployee, isEmployee,  user.isOwnProfile) {
                        ProfileTab.getTabs(user.isBusinessOrEmployee, isEmployee, user.isOwnProfile)
                    }

                    val pagerState = rememberPagerState(initialPage = currentTab) { tabs.size }
                    val currentTab = tabs[pagerState.currentPage]

                    val myBookmarks = viewModel.bookmarks.collectAsLazyPagingItems()

                    LaunchedEffect(pagerState) {
                        snapshotFlow {
                            pagerState.currentPage
                        }
                            .distinctUntilChanged()
                            .collectLatest { page -> viewModel.setCurrentTab(page) }
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
                                        viewModel.setCurrentTab(it)
                                        scope.launch { pagerState.animateScrollToPage(it) }
                                    },
                                    tabs = tabs
                                )

                                HorizontalPager(state = pagerState) { page ->
                                    when(tabs[page]) {
                                        ProfileTab.Posts -> {
                                            ProfilePostsTab(
                                                posts = myPosts,
                                                onNavigateToPost = { clickData ->
                                                    profileNavigate.toMyPostDetail(PostTabEnum.POSTS, clickData)
                                                }
                                            )
                                        }

                                        ProfileTab.Products -> {
                                            val products by viewModel.products.collectAsStateWithLifecycle()

                                            ProfileProductsTab(
                                                products = products,
                                                onNavigateToBookingFromProduct = {
                                                    profileNavigate.toBookingFromProduct(
                                                        it,
                                                        source = BookingSourceEnum.PROFILE
                                                    )
                                                },
                                                onNavigateToBookingFromProfile = {
                                                    if(user.businessId != null && user.businessOwner != null) {
                                                        profileNavigate.toBookingFromProfile(
                                                            NavigateBookingParam(
                                                                businessId = user.businessId,
                                                                userId = user.id,
                                                                businessOwnerId = user.businessOwner.id,
                                                                source = BookingSourceEnum.PROFILE,
                                                                selectedProductId = null
                                                            )
                                                        )
                                                    }
                                                }
                                            )
                                        }

                                        ProfileTab.Employees -> {
                                            val employees = viewModel.employees.collectAsLazyPagingItems()

                                            ProfileEmployeesTab(
                                                isOwnProfile = user.isOwnProfile,
                                                employees = employees,
                                                onNavigateToEmployeeProfile = { userId, username -> profileNavigate.toUserProfile(userId, username) },
                                            )
                                        }

                                        ProfileTab.Bookmarks -> {
                                            ProfileBookmarksTab(
                                                posts = myBookmarks,
                                                onNavigateToPost = { clickData ->
                                                    profileNavigate.toMyPostDetail(PostTabEnum.BOOKMARKS, clickData)
                                                }
                                            )
                                        }

                                        ProfileTab.Info -> {
                                            val about by viewModel.about.collectAsStateWithLifecycle()
                                            ProfileInfoTab(
                                                isEmployee = isEmployee,
                                                about = about,
                                                onNavigateToUserProfile = { userId, username ->
                                                    profileNavigate.toUserProfile(userId, username)
                                                },
                                            )
                                        }
                                    }
                                }
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
                                        isFollow = false,
                                        isFollowEnabled = false,
                                        onOpenScheduleSheet = { scope.launch { scheduleSheetState.show() } },
                                        onNavigateToSocial = { profileNavigate.toSocial(it) },
                                        onNavigateToBusinessOwner = {
                                            user.businessOwner?.let {
                                                profileNavigate.toUserProfile(it.id, it.username)
                                            }
                                        },
                                        onNavigateToEditProfile = { profileNavigate.toEditProfile() },
                                        onNavigateToMyCalendar = { profileNavigate.toMyCalendar() },
                                        onNavigateToBooking = { userId, businessId, businessOwnerId ->
                                            profileNavigate.toBookingFromProfile(
                                                NavigateBookingParam(
                                                    userId = userId,
                                                    businessId = businessId,
                                                    businessOwnerId = businessOwnerId,
                                                    selectedProductId = null,
                                                    source = BookingSourceEnum.PROFILE
                                                )
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MyProfileHeader(
    username: String,
    onNavigateToCamera: () -> Unit,
    onOpenMenu: () -> Unit
) {
    Header(
        title = "@$username",
        actions = {
            Row {
                Protected(permission = PermissionEnum.POST_CREATE) {
                    CustomIconButton(
                        painter = R.drawable.ic_circle_plus_outline,
                        onClick = onNavigateToCamera,
                        iconSize = IconSizeXL
                    )
                }

                CustomIconButton(
                    imageVector = Icons.Default.Menu,
                    onClick = onOpenMenu,
                    iconSize = IconSizeXL
                )
            }
        }
    )
}
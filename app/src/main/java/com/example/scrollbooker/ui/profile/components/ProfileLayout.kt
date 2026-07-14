package com.example.scrollbooker.ui.profile.components
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.rememberCollapsingNestedScroll
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.navigation.navigators.NavigateBookingParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.PostTabEnum
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileUserInfo
import com.example.scrollbooker.ui.profile.tabs.ProfileTab
import com.example.scrollbooker.ui.profile.tabs.ProfileTabRow
import com.example.scrollbooker.ui.profile.tabs.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tabs.employees.ProfileEmployeesTab
import com.example.scrollbooker.ui.profile.tabs.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tabs.posts.ProfilePostsTab
import com.example.scrollbooker.ui.profile.tabs.products.ProfileProductsTab
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileLayout(
    profile: FeatureState<UserProfile>,
    profileNavigate: ProfileNavigator,
    postsState: Flow<PagingData<Post>>,
    productsState: StateFlow<FeatureState<UserProducts>>,
    employeesState: Flow<PagingData<Employee>>,
    bookmarksState: Flow<PagingData<Post>>,
    aboutState: StateFlow<FeatureState<UserProfileAbout>>,
    onNavigateToPost: (SelectedPostUi) -> Unit,
    actions: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    var headerHeightPx by remember { mutableIntStateOf(0) }
    var headerOffset by remember { mutableFloatStateOf(0f) }

    val scheduleSheetState = rememberModalBottomSheetState()

    val nestedScrollConnection = rememberCollapsingNestedScroll(
        headerHeightPx = headerHeightPx,
        headerOffset = headerOffset,
        onHeaderOffsetChanged = { headerOffset = it }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        when (val profileData = profile) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> ProfileShimmer()
            is FeatureState.Success -> {
                val user = profileData.data
                val isEmployee = user.isBusinessOrEmployee && user.id != user.businessOwner?.id

                val tabs = remember(user.isBusinessOrEmployee, isEmployee,  user.isOwnProfile) {
                    ProfileTab.getTabs(user.isBusinessOrEmployee, isEmployee, user.isOwnProfile)
                }

                val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

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
                                    scope.launch { pagerState.animateScrollToPage(it) }
                                },
                                tabs = tabs
                            )

                            HorizontalPager(state = pagerState) { page ->
                                when(tabs[page]) {
                                    ProfileTab.Posts -> {
                                        val posts = postsState.collectAsLazyPagingItems()

                                        ProfilePostsTab(
                                            posts = posts,
                                            onNavigateToPost = onNavigateToPost
                                        )
                                    }

                                    ProfileTab.Products -> {
                                        val products by productsState.collectAsStateWithLifecycle()

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
                                        val employees = employeesState.collectAsLazyPagingItems()

                                        ProfileEmployeesTab(
                                            isOwnProfile = user.isOwnProfile,
                                            employees = employees,
                                            onNavigateToEmployeeProfile = { userId, username -> profileNavigate.toUserProfile(userId, username) },
                                        )
                                    }

                                    ProfileTab.Bookmarks -> {
                                        val bookmarks = bookmarksState.collectAsLazyPagingItems()

                                        ProfileBookmarksTab(
                                            posts = bookmarks,
                                            onNavigateToPost = { clickData ->
                                                profileNavigate.toMyPostDetail(PostTabEnum.BOOKMARKS, clickData)
                                            }
                                        )
                                    }

                                    ProfileTab.Info -> {
                                        val about by aboutState.collectAsStateWithLifecycle()

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
                                    onOpenScheduleSheet = { scope.launch { scheduleSheetState.show() } },
                                    onNavigateToSocial = { profileNavigate.toSocial(it) },
                                    onNavigateToBusinessOwner = {
                                        user.businessOwner?.let {
                                            profileNavigate.toUserProfile(it.id, it.username)
                                        }
                                    },
                                    actions = actions
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
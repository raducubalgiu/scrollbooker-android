package com.example.scrollbooker.ui.profile.components
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.customized.Refresh
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.rememberCollapsingNestedScroll
import com.example.scrollbooker.core.util.rememberFlingBehavior
import com.example.scrollbooker.entity.booking.employee.domain.model.Employee
import com.example.scrollbooker.entity.booking.products.domain.model.UserProducts
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.navigation.navigators.NavigateBookingParam
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.navigation.navigators.ProfilePostDetailParam
import com.example.scrollbooker.navigation.navigators.UserProfileParam
import com.example.scrollbooker.ui.profile.PostTabEnum
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileUserInfo
import com.example.scrollbooker.ui.profile.tabs.ProfileTab
import com.example.scrollbooker.ui.profile.tabs.ProfileTabRow
import com.example.scrollbooker.ui.profile.tabs.about.ProfileAboutTab
import com.example.scrollbooker.ui.profile.tabs.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tabs.employees.ProfileEmployeesTab
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
    isRefreshingState: StateFlow<Boolean>,
    onRefreshProfileAndTab: (currentTab: ProfileTab) -> Unit,
    onLoadFinished: () -> Unit,
    onNavigateToPost: (postIndex: Int, userId: Int) -> Unit,
    onOpenScheduleSheet: () -> Unit,
    actions: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val isRefreshing by isRefreshingState.collectAsStateWithLifecycle()

    var headerHeightPx by rememberSaveable { mutableIntStateOf(0) }
    var headerOffset by rememberSaveable { mutableFloatStateOf(0f) }

    val nestedScrollConnection = rememberCollapsingNestedScroll(
        headerHeightPx = headerHeightPx,
        headerOffset = headerOffset,
        onHeaderOffsetChanged = { headerOffset = it }
    )

    val headerNestedScrollDispatcher = remember { NestedScrollDispatcher() }
    val headerNestedScrollConnection = remember { object : NestedScrollConnection {} }

    fun dispatchHeaderScroll(delta: Float): Float {
        val preConsumed = headerNestedScrollDispatcher.dispatchPreScroll(
            available = Offset(0f, delta),
            source = NestedScrollSource.UserInput
        )
        val remaining = delta - preConsumed.y
        val postConsumed = headerNestedScrollDispatcher.dispatchPostScroll(
            consumed = Offset.Zero,
            available = Offset(0f, remaining),
            source = NestedScrollSource.UserInput
        )
        return preConsumed.y + postConsumed.y
    }

    val flingBehavior = rememberFlingBehavior()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val profileData = profile) {
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Loading -> ProfileShimmer()
            is FeatureState.Success -> {
                val user = profileData.data
                val isEmployee = user.isBusinessOrEmployee && user.id != user.businessOwner?.id

                val tabs = remember(user.id) {
                    ProfileTab.getTabs(
                        isBusinessOrEmployee = user.isBusinessOrEmployee,
                        isEmployee = isEmployee,
                        isMyProfile = user.isOwnProfile
                    )
                }

                val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

                val currentTab = remember(pagerState.currentPage, tabs) {
                    tabs.getOrNull(pagerState.currentPage)
                }

                val headerScrollableState = rememberScrollableState { delta ->
                    dispatchHeaderScroll(delta)
                }

                val tabsColumnScrollableState = rememberScrollableState { delta ->
                    0f
                }

                Refresh(
                    isRefreshing = isRefreshing,
                    onRefresh = { currentTab?.let { onRefreshProfileAndTab(it) } }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(nestedScrollConnection)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    translationY = headerHeightPx + headerOffset
                                }
                                .scrollable(
                                    orientation = Orientation.Vertical,
                                    state = tabsColumnScrollableState,
                                    flingBehavior = flingBehavior
                                )
                        ) {
                            ProfileTabRow(
                                selectedTabIndex = pagerState.currentPage,
                                onChangeTab = { scope.launch { pagerState.animateScrollToPage(it) } },
                                tabs = tabs
                            )

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.weight(1f),
                                beyondViewportPageCount = 0
                            ) { page ->
                                when (tabs[page]) {
                                    ProfileTab.Posts -> {
                                        val posts = postsState.collectAsLazyPagingItems()

                                        ProfilePostsTab(
                                            posts = posts,
                                            onNavigateToPost = onNavigateToPost,
                                            onLoadFinished = onLoadFinished
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
                                                if (user.businessId != null && user.businessOwner != null) {
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
                                            onNavigateToEmployeeProfile = { profileNavigate.toUserProfile(it) },
                                            onNavigateToBooking = { employee ->
                                                if (user.businessId != null && user.businessOwner != null) {
                                                    profileNavigate.toBookingFromProfile(
                                                        NavigateBookingParam(
                                                            businessId = user.businessId,
                                                            userId = employee.id,
                                                            businessOwnerId = user.businessOwner.id,
                                                            source = BookingSourceEnum.PROFILE,
                                                            selectedProductId = null
                                                        )
                                                    )
                                                }
                                            }
                                        )
                                    }

                                    ProfileTab.Bookmarks -> {
                                        val bookmarks = bookmarksState.collectAsLazyPagingItems()

                                        ProfileBookmarksTab(
                                            posts = bookmarks,
                                            onNavigateToPost = { postIndex, userId ->
                                                profileNavigate.toMyPostDetail(
                                                    ProfilePostDetailParam(
                                                        postTab = PostTabEnum.BOOKMARKS.key,
                                                        postIndex = postIndex,
                                                        userId = userId,
                                                    )
                                                )
                                            }
                                        )
                                    }

                                    ProfileTab.About -> {
                                        val about by aboutState.collectAsStateWithLifecycle()

                                        ProfileAboutTab(
                                            isEmployee = isEmployee,
                                            about = about,
                                            onNavigateToUserProfile = { profileNavigate.toUserProfile(it) }
                                        )
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset { IntOffset(0, headerOffset.roundToInt()) }
                                .nestedScroll(
                                    connection = headerNestedScrollConnection,
                                    dispatcher = headerNestedScrollDispatcher
                                )
                                .scrollable(
                                    orientation = Orientation.Vertical,
                                    state = headerScrollableState,
                                    flingBehavior = flingBehavior
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onSizeChanged { size -> headerHeightPx = size.height }
                            ) {
                                ProfileUserInfo(
                                    user = user,
                                    onOpenScheduleSheet = onOpenScheduleSheet,
                                    onNavigateToSocial = { profileNavigate.toSocial(it) },
                                    onNavigateToBusinessOwner = {
                                        user.businessOwner?.let {
                                            profileNavigate.toUserProfile(
                                                UserProfileParam(it.id, it.username, it.profession)
                                            )
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

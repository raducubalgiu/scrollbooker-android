package com.example.scrollbooker.ui.profile
import BottomBar
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.customized.Protected.Protected
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.Dimens.IconSizeXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.UserPermissionsController
import com.example.scrollbooker.ui.profile.components.sheets.ProfileMenuSheet
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileShimmer
import com.example.scrollbooker.ui.profile.components.userInfo.ProfileUserInfo
import com.example.scrollbooker.ui.profile.tabs.ProfileTab
import com.example.scrollbooker.ui.profile.tabs.ProfileTabRow
import com.example.scrollbooker.ui.profile.tabs.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tabs.employees.ProfileEmployeesTab
import com.example.scrollbooker.ui.profile.tabs.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tabs.posts.ProfilePostsTab
import com.example.scrollbooker.ui.profile.tabs.reposts.ProfileRepostsTab
import com.example.scrollbooker.ui.theme.Background
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Stable
class ProfileScrollCoordinator {
    var isAtTop by mutableStateOf(true)
        private set

    fun updateIsAtTop(value: Boolean) {
        if (isAtTop != value) isAtTop = value
    }
}

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
    val menuSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if(menuSheetState.isVisible) {
        ProfileMenuSheet(
            sheetState = menuSheetState,
            profileNavigate = profileNavigate,
            permissionController = permissionController
        )
    }

    val userData = (myProfileData as? FeatureState.Success)?.data

    var headerHeightPx by remember { mutableIntStateOf(0) }
    val headerOffset by viewModel.headerOffset.collectAsStateWithLifecycle()

    val scrollCoordinator = remember { ProfileScrollCoordinator() }

    val nestedScrollConnection = remember(headerHeightPx, scrollCoordinator) {
        object : NestedScrollConnection {

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val min = -headerHeightPx.toFloat()
                val max = 0f

                // ✅ Collapse header first when scrolling UP
                if (delta < 0 && headerOffset > min) {
                    val newOffset = (headerOffset + delta).coerceIn(min, max)
                    val consumed = newOffset - headerOffset
                    viewModel.setHeaderOffset(newOffset)
                    return Offset(0f, consumed)
                }

                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                val min = -headerHeightPx.toFloat()
                val max = 0f

                // ✅ Expand header with the leftover scroll AFTER child reached top
                if (delta > 0 && scrollCoordinator.isAtTop && headerOffset < max) {
                    val newOffset = (headerOffset + delta).coerceIn(min, max)
                    val consumedByHeader = newOffset - headerOffset
                    viewModel.setHeaderOffset(newOffset)
                    return Offset(0f, consumedByHeader)
                }

                return Offset.Zero
            }
        }
    }

    Scaffold(
        topBar = {
            Header(
                title = "@${userData?.username ?: ""}",
                actions = {
                    Row {
                        Protected(permission = PermissionEnum.POST_CREATE) {
                            CustomIconButton(
                                painter = R.drawable.ic_circle_plus_outline,
                                onClick = { profileNavigate.toCamera() },
                                iconSize = IconSizeXL
                            )
                        }

                        CustomIconButton(
                            imageVector = Icons.Default.Menu,
                            onClick = { scope.launch { menuSheetState.show() } },
                            iconSize = IconSizeXL
                        )
                    }
                }
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

                    val tabs = remember(user.isBusinessOrEmployee, user.isOwnProfile) {
                        ProfileTab.getTabs(user.isBusinessOrEmployee, user.isOwnProfile)
                    }

                    val pagerState = rememberPagerState(initialPage = 0) { tabs.size }

                    PullToRefreshBox(
                        isRefreshing = false,
                        onRefresh = {
                            viewModel.loadUserProfile()
                        }
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
                                    pagerState = pagerState,
                                    tabs = tabs
                                )

                                HorizontalPager(state = pagerState) { page ->
                                    when(tabs[page]) {
                                        ProfileTab.Posts -> ProfilePostsTab(
                                            posts = myPosts,
                                            onUpdateTop = { scrollCoordinator.updateIsAtTop(it) },
                                            onNavigateToPost = { postUi, post ->
                                                navigateToPost(viewModel, profileNavigate, postUi, post)
                                            }
                                        )
                                        ProfileTab.Products -> {
                                            val gridState = rememberLazyGridState()

                                            LaunchedEffect(gridState) {
                                                snapshotFlow {
                                                    gridState.firstVisibleItemIndex == 0 && gridState.firstVisibleItemScrollOffset == 0
                                                }.collect { atTop ->
                                                    scrollCoordinator.updateIsAtTop(atTop)
                                                }
                                            }

                                            LazyVerticalGrid(
                                                state = gridState,
                                                columns = GridCells.Fixed(3),
                                                verticalArrangement = Arrangement.spacedBy(1.dp),
                                                horizontalArrangement = Arrangement.spacedBy(1.dp),
                                                modifier = Modifier.fillMaxSize()
                                            ) {
                                                items(50) {
                                                    Box(modifier = Modifier
                                                        .aspectRatio(9f / 12f)
                                                        .background(Color.Gray)
                                                    ) {}
                                                }
                                            }
                                        }
                                        ProfileTab.Employees -> ProfileEmployeesTab(
                                            viewModel = viewModel,
                                            onUpdateTop = { scrollCoordinator.updateIsAtTop(it) },
                                            onNavigateToEmployeeProfile = { profileNavigate.toUserProfile(it) },
                                        )
                                        ProfileTab.Bookmarks -> ProfileBookmarksTab(
                                            viewModel = viewModel,
                                            onUpdateTop = { scrollCoordinator.updateIsAtTop(it) },
                                            onNavigateToPost = { postUi, post ->
                                                navigateToPost(viewModel, profileNavigate, postUi, post)
                                            }
                                        )
                                        ProfileTab.Reposts -> ProfileRepostsTab(
                                            viewModel = viewModel,
                                            onUpdateTop = { scrollCoordinator.updateIsAtTop(it) },
                                            onNavigateToPost = { postUi, post ->
                                                navigateToPost(viewModel, profileNavigate, postUi, post)
                                            }
                                        )
                                        ProfileTab.Info -> ProfileInfoTab(
                                            viewModel = viewModel,
                                            onUpdateTop = { scrollCoordinator.updateIsAtTop(it) },
                                        )
                                    }
                                }
                            }

                            // 2. Header-ul mobil (se mișcă cu headerOffsetHeightPx)
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
                                        onOpenScheduleSheet = { },
                                        onNavigateToBusinessOwner = {
                                            it?.let { profileNavigate.toUserProfile(it) }
                                        },
                                        onNavigateToSocial = { profileNavigate.toSocial(it) },
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
}

private fun navigateToPost(
    viewModel: MyProfileViewModel,
    profileNavigate: ProfileNavigator,
    postUi: SelectedPostUi,
    post: Post
) {
    viewModel.onPageSettled(postUi.index)

    viewModel.ensureImmediate(
        centerIndex = postUi.index,
        getPost = { i -> if(i == postUi.index) post else null }
    )

    viewModel.setSelectedPost(postUi)
    profileNavigate.toPostDetail()
}
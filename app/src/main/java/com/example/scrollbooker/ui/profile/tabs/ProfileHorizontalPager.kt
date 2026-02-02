package com.example.scrollbooker.ui.profile.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.navigation.navigators.ProfileNavigator
import com.example.scrollbooker.ui.profile.components.ProfileLayoutViewModel
import com.example.scrollbooker.ui.profile.components.SelectedPostUi
import com.example.scrollbooker.ui.profile.tabs.bookmarks.ProfileBookmarksTab
import com.example.scrollbooker.ui.profile.tabs.employees.ProfileEmployeesTab
import com.example.scrollbooker.ui.profile.tabs.info.ProfileInfoTab
import com.example.scrollbooker.ui.profile.tabs.posts.ProfilePostsTab

@Composable
fun ProfileHorizontalPager(
    layoutViewModel: ProfileLayoutViewModel,
    pagerState: PagerState,
    profileNavigate: ProfileNavigator,
    tabs: List<ProfileTab>,
    onNavigateToPost: (SelectedPostUi, Post) -> Unit,
    posts: LazyPagingItems<Post>
) {
    HorizontalPager(state = pagerState) { page ->
        when(tabs[page]) {
            ProfileTab.Posts -> ProfilePostsTab(
                posts = posts,
                onNavigateToPost = onNavigateToPost
            )
            ProfileTab.Products -> {
                LazyVerticalGrid(
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
                viewModel = layoutViewModel,
                onNavigateToEmployeeProfile = { profileNavigate.toUserProfile(it) },
            )
            ProfileTab.Bookmarks -> ProfileBookmarksTab(
                viewModel = layoutViewModel,
                onNavigateToPost = onNavigateToPost
            )
            ProfileTab.Info -> ProfileInfoTab(viewModel = layoutViewModel)
        }
    }
}
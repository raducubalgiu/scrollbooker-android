package com.example.scrollbooker.ui.social.tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.ui.social.SocialViewModel
import com.example.scrollbooker.ui.social.components.UserSocialList

@Composable
fun UserFollowersTab(
    viewModel: SocialViewModel,
    onNavigateUserProfile: (Int) -> Unit
) {
    val userFollowers = viewModel.userFollowers.collectAsLazyPagingItems()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val followedOverrides by viewModel.followedOverrides.collectAsState()
    val followRequestLocks by viewModel.followRequestLocks.collectAsState()

    UserSocialList(
        users = userFollowers,
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshFollowers() },
        followedOverrides = followedOverrides,
        followRequestLocks = followRequestLocks,
        onFollow = { isFollowed, userId ->
            viewModel.onFollow(isFollowed, userId)
        },
        onNavigateUserProfile = onNavigateUserProfile
    )
}
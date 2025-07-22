package com.example.scrollbooker.ui.profile.social.tab.followers
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.ui.profile.social.UserSocialViewModel
import com.example.scrollbooker.ui.profile.social.components.UserSocialList

@Composable
fun UserFollowersTab(
    viewModel: UserSocialViewModel,
    onNavigateUserProfile: (Int) -> Unit
) {
    val userFollowers = viewModel.userFollowers.collectAsLazyPagingItems()
    val followedOverrides by viewModel.followedOverrides.collectAsState()
    val followRequestLocks by viewModel.followRequestLocks.collectAsState()

    UserSocialList(
        users = userFollowers,
        followedOverrides = followedOverrides,
        followRequestLocks = followRequestLocks,
        onFollow = { isFollowed, userId ->
            viewModel.onFollow(isFollowed, userId)
        },
        onNavigateUserProfile = onNavigateUserProfile
    )
}
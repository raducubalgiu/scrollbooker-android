package com.example.scrollbooker.ui.social.tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.ui.social.UserSocialViewModel
import com.example.scrollbooker.ui.social.components.UserSocialList

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
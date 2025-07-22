package com.example.scrollbooker.ui.profile.social.tab.followings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.ui.profile.social.UserSocialViewModel
import com.example.scrollbooker.ui.profile.social.components.UserSocialList

@Composable
fun UserFollowingsTab(
    viewModal: UserSocialViewModel,
    onNavigateUserProfile: (Int) -> Unit
) {
    val userFollowings = viewModal.userFollowings.collectAsLazyPagingItems()
    val followedOverrides by viewModal.followedOverrides.collectAsState()
    val followRequestLocks by viewModal.followRequestLocks.collectAsState()

    UserSocialList(
        users = userFollowings,
        followedOverrides = followedOverrides,
        followRequestLocks = followRequestLocks,
        onFollow = { isFollowed, userId ->
            viewModal.onFollow(isFollowed, userId)
        },
        onNavigateUserProfile = onNavigateUserProfile
    )
}
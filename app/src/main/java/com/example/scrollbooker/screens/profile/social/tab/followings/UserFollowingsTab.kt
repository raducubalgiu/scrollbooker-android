package com.example.scrollbooker.screens.profile.social.tab.followings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.screens.profile.social.UserSocialViewModel
import com.example.scrollbooker.screens.profile.social.components.UserSocialList

@Composable
fun UserFollowingsTab(
    viewModal: UserSocialViewModel,
    onNavigateUserProfile: (Int) -> Unit
) {
    val userFollowings = viewModal.userFollowings.collectAsLazyPagingItems()
    val followedOverrides by viewModal.followedOverrides.collectAsState()
    val followRequestLocks by viewModal.followRequestLocks.collectAsState()

    UserSocialList(
        pagingItems = userFollowings,
        followedOverrides = followedOverrides,
        followRequestLocks = followRequestLocks,
        onFollow = { isFollowed, userId ->
            viewModal.onFollow(isFollowed, userId)
        },
        onNavigateUserProfile = onNavigateUserProfile
    )
}
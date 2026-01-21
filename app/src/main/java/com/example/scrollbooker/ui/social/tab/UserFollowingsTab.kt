package com.example.scrollbooker.ui.social.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.ui.social.SocialViewModel
import com.example.scrollbooker.ui.social.components.UserSocialList

@Composable
fun UserFollowingsTab(
    viewModal: SocialViewModel,
    onNavigateUserProfile: (Int) -> Unit
) {
    val userFollowings = viewModal.userFollowings.collectAsLazyPagingItems()
    val refreshState = userFollowings.loadState.refresh

    val isInitialLoading = refreshState is LoadState.Loading && userFollowings.itemCount == 0

    val followedOverrides by viewModal.followedOverrides.collectAsState()
    val followRequestLocks by viewModal.followRequestLocks.collectAsState()

    when {
        isInitialLoading -> LoadingScreen()
        refreshState is LoadState.Error -> ErrorScreen()
        else -> {
            if(userFollowings.itemCount == 0) {
                MessageScreen(
                    message = stringResource(R.string.dontFoundResults),
                    icon = painterResource(R.drawable.ic_users_outline)
                )
            } else {
                UserSocialList(
                    users = userFollowings,
                    onRefresh = { viewModal.refreshFollowings() },
                    followedOverrides = followedOverrides,
                    followRequestLocks = followRequestLocks,
                    onFollow = { isFollowed, userId ->
                        viewModal.onFollow(isFollowed, userId)
                    },
                    onNavigateUserProfile = onNavigateUserProfile
                )
            }
        }
    }
}
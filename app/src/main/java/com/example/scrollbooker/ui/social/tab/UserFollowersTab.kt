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
import com.example.scrollbooker.ui.inbox.components.NotificationsList
import com.example.scrollbooker.ui.social.SocialViewModel
import com.example.scrollbooker.ui.social.components.UserSocialList

@Composable
fun UserFollowersTab(
    viewModel: SocialViewModel,
    onNavigateUserProfile: (Int) -> Unit
) {
    val userFollowers = viewModel.userFollowers.collectAsLazyPagingItems()
    val refreshState = userFollowers.loadState.refresh

    val isInitialLoading = refreshState is LoadState.Loading && userFollowers.itemCount == 0

    val followedOverrides by viewModel.followedOverrides.collectAsState()
    val followRequestLocks by viewModel.followRequestLocks.collectAsState()

    when {
        isInitialLoading -> LoadingScreen()
        refreshState is LoadState.Error -> ErrorScreen()
        else -> {
            if(userFollowers.itemCount == 0) {
                MessageScreen(
                    message = stringResource(R.string.dontFoundResults),
                    icon = painterResource(R.drawable.ic_users_outline)
                )
            } else {
                UserSocialList(
                    users = userFollowers,
                    onRefresh = { viewModel.refreshFollowers() },
                    followedOverrides = followedOverrides,
                    followRequestLocks = followRequestLocks,
                    onFollow = { isFollowed, userId ->
                        viewModel.onFollow(isFollowed, userId)
                    },
                    onNavigateUserProfile = onNavigateUserProfile
                )
            }
        }
    }
}
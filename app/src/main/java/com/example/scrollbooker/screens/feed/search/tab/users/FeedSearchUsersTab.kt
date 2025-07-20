package com.example.scrollbooker.screens.feed.search.tab.users
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.screens.profile.social.components.UserSocialList

@Composable
fun FeedSearchUsersTab(
    query: String,
    viewModel: FeedSearchUsersViewModel
) {
    LaunchedEffect(query) {
        viewModel.setQuery(query)
    }

    val users = viewModel.usersState.collectAsLazyPagingItems()

    val followedOverrides by viewModel.followedOverrides.collectAsState()
    val followRequestLocks by viewModel.followRequestLocks.collectAsState()

    UserSocialList(
        pagingItems = users,
        followedOverrides = followedOverrides,
        followRequestLocks = followRequestLocks,
        onFollow = { isFollowed, userId ->
            viewModel.onFollow(isFollowed, userId)
        },
        onNavigateUserProfile = {}
    )
}
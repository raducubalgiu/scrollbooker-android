package com.example.scrollbooker.ui.social.components
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSocialList(
    users: LazyPagingItems<UserSocial>,
    onRefresh: () -> Unit,
    followedOverrides: Map<Int, Boolean>,
    followRequestLocks: Set<Int>,
    onFollow: (Boolean, Int) -> Unit,
    onNavigateUserProfile: (Int) -> Unit
) {
    val appendState = users.loadState.append

    PullToRefreshBox(
        isRefreshing = users.loadState.refresh is LoadState.Loading,
        onRefresh = onRefresh
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(users.itemCount) { index ->
                users[index]?.let { userSocial ->
                    val isLocked = followRequestLocks.contains(userSocial.id)

                    UserSocialItem(
                        userSocial = userSocial,
                        enabled = !isLocked,
                        isFollowedOverrides = followedOverrides[userSocial.id],
                        onFollow = { isFollowed -> onFollow(isFollowed, userSocial.id) },
                        onNavigateUserProfile = onNavigateUserProfile
                    )
                }
            }

            item {
                when(appendState) {
                    is LoadState.Error -> Text("A aparut o eroare")
                    is LoadState.Loading -> LoadMoreSpinner()
                    is LoadState.NotLoading -> Unit
                }
            }

            item {
                Spacer(modifier = Modifier
                    .fillMaxSize()
                    .height(
                        WindowInsets.safeContent
                            .only(WindowInsetsSides.Bottom)
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
                )
            }
        }
    }
}
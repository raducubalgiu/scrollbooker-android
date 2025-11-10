package com.example.scrollbooker.ui.social.components

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSocialList(
    users: LazyPagingItems<UserSocial>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    followedOverrides: Map<Int, Boolean>,
    followRequestLocks: Set<Int>,
    onFollow: (Boolean, Int) -> Unit,
    onNavigateUserProfile: (Int) -> Unit
) {
    val refreshState = users.loadState.refresh
    val appendState = users.loadState.append

    Box(modifier = Modifier.fillMaxSize()) {
        when(refreshState) {
            is LoadState.Loading -> { LoadingScreen() }
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> {
                if(users.itemCount == 0) {
                    MessageScreen(
                        message = stringResource(R.string.dontFoundResults),
                        icon = painterResource(R.drawable.ic_users_outline)
                    )
                } else {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
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
            }
        }
    }
}
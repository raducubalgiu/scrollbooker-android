package com.example.scrollbooker.ui.profile.social.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

@Composable
fun UserSocialList(
    users: LazyPagingItems<UserSocial>,
    followedOverrides: Map<Int, Boolean>,
    followRequestLocks: Set<Int>,
    onFollow: (Boolean, Int) -> Unit,
    onNavigateUserProfile: (Int) -> Unit
) {
    users.apply {
        when(loadState.refresh) {
            is LoadState.Loading -> LoadingScreen()
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> {
                if(users.itemCount == 0) {
                    MessageScreen(
                        message = stringResource(R.string.dontFoundResults),
                        icon = painterResource(R.drawable.ic_users_outline)
                    )
                }
            }
        }
    }

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

        users.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item { LoadMoreSpinner() }
                }

                is LoadState.Error -> {
                    item { Text("Ceva nu a mers cum trebuie") }
                }

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
package com.example.scrollbooker.ui.inbox.components
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.components.customized.UserListItem
import com.example.scrollbooker.core.enums.NotificationTypeEnum
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.ui.inbox.InboxViewModel
import com.example.scrollbooker.ui.social.components.UserSocialItem
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import timber.log.Timber

@Composable
fun NotificationsList(
    viewModel: InboxViewModel,
    notifications: LazyPagingItems<Notification>,
    onFollow: (Boolean, Int) -> Unit,
    inboxNavigate: InboxNavigator
) {
    val followState by viewModel.followState.collectAsState()
    val isFollowSaving by viewModel.isFollowSaving.collectAsState()

    val appendState = notifications.loadState.append

    LazyColumn {
        items(
            count = notifications.itemCount,
            key = { index -> notifications[index]?.id ?: index }
        ) { index ->
            val notification = notifications[index]
            notification?.let {
                val isLocked = isFollowSaving.contains(notification.sender.id)

                when(it.type) {
                    NotificationTypeEnum.FOLLOW -> {
                        NotificationFollowItem(
                            isFollow = followState[notification.sender.id],
                            notification = notification,
                            enabled = !isLocked,
                            onFollow = { isFollowed -> onFollow(isFollowed, notification.sender.id) },
                            onNavigateUserProfile = { inboxNavigate.toUserProfile(it) }
                        )
                    }
                    NotificationTypeEnum.EMPLOYMENT_REQUEST -> {
                        UserListItem(
                            title = it.sender.fullName ?: "",
                            description = stringResource(R.string.sentYouAnEmploymentRequest),
                            avatar = it.sender.avatar ?: "",
                            rating = 4.5f,
                            isEnabled = true,
                            isBusinessOrEmployee = false,
                            onNavigateUserProfile = {  },
                            trailingContent = {
                                MainButtonSmall(
                                    title = stringResource(R.string.seeMore),
                                    onClick = { inboxNavigate.toEmploymentRespond(it.id) },
                                    colors = ButtonColors(
                                        containerColor = Error,
                                        contentColor = OnError,
                                        disabledContainerColor = Divider,
                                        disabledContentColor = OnSurfaceBG
                                    )
                                )
                            }
                        )
                    }
                    NotificationTypeEnum.UNKNOWN -> Unit
                }
            }
        }

        item {
            if (appendState is LoadState.Loading) {
                LoadMoreSpinner()
            }
        }
    }
}
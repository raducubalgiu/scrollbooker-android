package com.example.scrollbooker.ui.inbox.components
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonSmall
import com.example.scrollbooker.components.customized.UserListItem
import com.example.scrollbooker.core.enums.NotificationTypeEnum
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.ui.inbox.InboxViewModel
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.OnSurfaceBG

@OptIn(ExperimentalMaterial3Api::class)
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

    PullToRefreshBox(
        isRefreshing = notifications.loadState.refresh is LoadState.Loading,
        onRefresh = { viewModel.refreshNotifications() }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(
                count = notifications.itemCount,
                key = { index -> notifications[index]?.id ?: index }
            ) { index ->
                val notification = notifications[index]
                notification?.let { notification ->
                    val isLocked = isFollowSaving.contains(notification.sender.id)

                    when(notification.type) {
                        NotificationTypeEnum.FOLLOW -> {
                            NotificationFollowItem(
                                modifier = Modifier.animateItem(),
                                isFollow = followState[notification.sender.id],
                                notification = notification,
                                enabled = !isLocked,
                                onFollow = { isFollowed -> onFollow(isFollowed, notification.sender.id) },
                                onNavigateUserProfile = { inboxNavigate.toUserProfile(it) }
                            )
                        }
                        NotificationTypeEnum.EMPLOYMENT_REQUEST -> {
                            UserListItem(
                                title = notification.sender.fullName ?: "",
                                description = stringResource(R.string.sentYouAnEmploymentRequest),
                                avatar = notification.sender.avatar ?: "",
                                rating = 4.5f,
                                isEnabled = true,
                                isBusinessOrEmployee = false,
                                onNavigateUserProfile = { inboxNavigate.toUserProfile(notification.senderId) },
                                trailingContent = {
                                    MainButtonSmall(
                                        title = stringResource(R.string.seeMore),
                                        onClick = {
                                            notification.data?.employmentRequestId?.let { empId ->
                                                inboxNavigate.toEmploymentRespond(empId)
                                            }
                                        },
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
                        NotificationTypeEnum.EMPLOYMENT_REQUEST_ACCEPT -> {
                            UserListItem(
                                title = notification.sender.fullName ?: "",
                                description = stringResource(R.string.acceptedYourEmploymentRequest),
                                avatar = notification.sender.avatar ?: "",
                                rating = 4.5f,
                                isEnabled = true,
                                isBusinessOrEmployee = false,
                                onNavigateUserProfile = { inboxNavigate.toUserProfile(notification.senderId) },
                            )
                        }
                        NotificationTypeEnum.EMPLOYMENT_REQUEST_DENIED -> {
                            UserListItem(
                                title = notification.sender.fullName ?: "",
                                description = stringResource(R.string.deniedYourEmploymentRequest),
                                avatar = notification.sender.avatar ?: "",
                                rating = 4.5f,
                                isEnabled = true,
                                isBusinessOrEmployee = false,
                                onNavigateUserProfile = { inboxNavigate.toUserProfile(notification.senderId) },
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
}
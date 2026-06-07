package com.example.scrollbooker.ui.inbox.components
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ShapeDefaults
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
import com.example.scrollbooker.components.customized.Refresh
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

    Refresh(
        isRefreshing = notifications.loadState.refresh is LoadState.Loading,
        onRefresh = { viewModel.refreshNotifications() }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(notifications.itemCount) { index ->
                val notification = notifications[index]

                notification?.let { notif ->
                    val senderId = notif.sender.id
                    val isLocked = isFollowSaving.contains(senderId)

                    NotificationItem(
                        modifier = Modifier.animateItem(),
                        notification = notif,
                        isLocked = isLocked,
                        isFollowed = followState[senderId],
                        onFollow = { isFollowed -> onFollow(isFollowed, senderId) },
                        inboxNavigate = inboxNavigate
                    )
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

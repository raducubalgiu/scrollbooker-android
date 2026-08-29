package com.example.scrollbooker.ui.inbox.components
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.components.customized.Refresh
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.ui.inbox.InboxViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsList(
    viewModel: InboxViewModel,
    notifications: LazyPagingItems<Notification>,
    onFollow: (Boolean, Int) -> Unit,
    inboxNavigate: InboxNavigator
) {
    val followState by viewModel.followState.collectAsStateWithLifecycle()
    val isFollowSaving by viewModel.isFollowSaving.collectAsStateWithLifecycle()
    val readState by viewModel.readState.collectAsStateWithLifecycle()
    val appendState = notifications.loadState.append

    Refresh(
        isRefreshing = notifications.loadState.refresh is LoadState.Loading,
        onRefresh = { viewModel.refreshNotifications() }
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(
                count = notifications.itemCount,
                key = { index -> notifications[index]?.id ?: index }
            ) { index ->
                val notification = notifications[index]

                notification?.let { notif ->
                    val senderId = notif.sender.id
                    val isLocked = isFollowSaving.contains(senderId)
                    val isFollowed = followState[senderId] ?: notif.sender.isFollow
                    val isRead = notif.isRead || readState.contains(notif.id)

                    NotificationItem(
                        notification = notif,
                        isRead = isRead,
                        isLocked = isLocked,
                        isFollowed = isFollowed,
                        onFollow = { isFollowedTarget -> onFollow(isFollowedTarget, senderId) },
                        onSeen = { viewModel.markAsSeen(it) },
                        inboxNavigate = inboxNavigate
                    )
                }
            }

            if (appendState is LoadState.Loading) {
                item {
                    LoadMoreSpinner()
                }
            }
        }
    }
}

package com.example.scrollbooker.ui.inbox.components
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.user.notification.domain.model.Notification
import timber.log.Timber

@Composable
fun NotificationsList(
    notifications: LazyPagingItems<Notification>,
    onNavigate: (Int) -> Unit
) {
    val appendState = notifications.loadState.append

    LazyColumn {
        items(
            count = notifications.itemCount,
            key = { index -> notifications[index]?.id ?: index }
        ) { index ->
            val notification = notifications[index]
            notification?.let {
                when(it.type) {
                    "follow" -> {
                        NotificationItem(
                            fullName = it.sender.fullName.toString(),
                            message = stringResource(id = R.string.startedFollowingYou),
                            avatar = it.sender.avatar.toString(),
                            actionTitle = stringResource(R.string.follow),
                        )
                    }
                    "employment_request" -> {
                        NotificationItem(
                            fullName = it.sender.fullName.toString(),
                            message = stringResource(R.string.sentYouAnEmploymentRequest),
                            avatar = it.sender.avatar.toString(),
                            actionTitle = stringResource(R.string.seeMore),
                            onActionClick = { onNavigate(it.id) }
                        )
                    }
                    else -> Unit
                }
            }
        }

        when(appendState) {
            is LoadState.Loading -> {
                item { LoadMoreSpinner() }
            }
            is LoadState.Error -> {
                val error = appendState.error
                Timber.tag("Notifications").e("ERROR: on Fetching Append Notifications $error")
                item { Text("Eroare la reincarcare") }
            }
            else -> Unit
        }
    }
}
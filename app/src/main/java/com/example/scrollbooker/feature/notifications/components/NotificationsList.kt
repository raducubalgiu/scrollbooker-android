package com.example.scrollbooker.feature.notifications.components
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.notifications.domain.model.Notification
import timber.log.Timber

@Composable
fun NotificationsList(notifications: LazyPagingItems<Notification>) {
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
                            actionTitle = stringResource(R.string.follow)
                        )
                    }
                    "employment_request" -> {
                        NotificationItem(
                            fullName = it.sender.fullName.toString(),
                            message = stringResource(R.string.sentYouAnEmploymentRequest),
                            avatar = it.sender.avatar.toString(),
                            actionTitle = stringResource(R.string.seeMore)
                        )
                    }
                    else -> Unit
                }
            }
        }

        when(appendState) {
            is LoadState.Loading -> {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(BasePadding)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
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
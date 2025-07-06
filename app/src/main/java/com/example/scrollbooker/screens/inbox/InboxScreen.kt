package com.example.scrollbooker.screens.inbox
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.screens.inbox.components.NotificationsList

@Composable
fun InboxScreen(
    viewModel: InboxViewModel,
    onNavigate: (Int) -> Unit
) {
    val notifications = viewModel.notifications.collectAsLazyPagingItems()
    val refreshState = notifications.loadState.refresh

    Layout(
        modifier = Modifier.statusBarsPadding(),
        headerTitle = stringResource(id = R.string.inbox),
        enableBack = false,
        enablePaddingH = false,
        enablePaddingV = false
    ) {
        if(refreshState is LoadState.NotLoading) {
            NotificationsList(
                notifications=notifications,
                onNavigate = onNavigate
            )
        }
    }

    when(refreshState) {
        is LoadState.Loading -> { LoadingScreen() }
        is LoadState.Error -> ErrorScreen()
        is LoadState.NotLoading -> {
            if(notifications.itemCount == 0) {
                MessageScreen(
                    message = "Nu ai nici o notificare",
                    icon = painterResource(R.drawable.ic_notifications_alert_outline)
                )
            }
        }
    }
}
package com.example.scrollbooker.screens.inbox
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun InboxScreen(viewModel: InboxViewModel) {
    val notifications = viewModel.notifications.collectAsLazyPagingItems()
    val refreshState = notifications.loadState.refresh

    Layout(
        headerTitle = stringResource(id = R.string.inbox),
        enableBack = false,
        enablePaddingH = false,
        enablePaddingV = false
    ) { if(refreshState is LoadState.NotLoading) NotificationsList(notifications) }

    Box(Modifier.fillMaxSize()) {
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
}
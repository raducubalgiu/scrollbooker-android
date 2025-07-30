package com.example.scrollbooker.ui.inbox
import BottomBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.host.InboxNavHost
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.inbox.components.NotificationsList

@Composable
fun InboxScreen(
    viewModel: InboxViewModel,
    onNavigate: (Int) -> Unit,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit
) {
    val notifications = viewModel.notifications.collectAsLazyPagingItems()
    val refreshState = notifications.loadState.refresh

    Scaffold(
        bottomBar = {
            BottomBar(
                appointmentsNumber = appointmentsNumber,
                currentTab = MainTab.Inbox,
                currentRoute = MainRoute.Inbox.route,
                onNavigate = onChangeTab
            )
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Header(
                title = stringResource(id = R.string.inbox),
                enableBack = false
            )

            if(refreshState is LoadState.NotLoading) {
                NotificationsList(
                    notifications=notifications,
                    onNavigate = onNavigate
                )
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
    }
}
package com.example.scrollbooker.ui.inbox
import BottomBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.inbox.components.NotificationsList

@Composable
fun InboxScreen(
    viewModel: InboxViewModel,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit,
    inboxNavigate: InboxNavigator
) {
    val notifications = viewModel.notifications.collectAsLazyPagingItems()
    val refreshState = notifications.loadState.refresh

    Scaffold(
        bottomBar = {
            BottomBar(
                appointmentsNumber = appointmentsNumber,
                currentTab = MainTab.Inbox,
                currentRoute = MainRoute.Inbox.route,
                onChangeTab = onChangeTab
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Column(Modifier.fillMaxSize()) {
                Header(
                    title = stringResource(id = R.string.inbox),
                    enableBack = false
                )

                when(refreshState) {
                    is LoadState.Loading -> { LoadingScreen() }
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.NotLoading -> {
                        NotificationsList(
                            viewModel = viewModel,
                            notifications = notifications,
                            onFollow = { isFollowed, userId ->
                                viewModel.follow(isFollowed, userId)
                            },
                            inboxNavigate = inboxNavigate
                        )
                    }
                }
            }

            if(refreshState is LoadState.NotLoading && notifications.itemCount == 0) {
                MessageScreen(
                    message = stringResource(R.string.dontHaveAnyNotification),
                    icon = painterResource(R.drawable.ic_notifications_alert_outline)
                )
            }
        }
    }
}
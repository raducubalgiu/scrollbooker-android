package com.example.scrollbooker.ui.inbox
import BottomBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.scrollbooker.navigation.navigators.InboxNavigator
import com.example.scrollbooker.ui.inbox.components.NotificationsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    viewModel: InboxViewModel,
    inboxNavigate: InboxNavigator
) {
    val notifications = viewModel.notifications.collectAsLazyPagingItems()
    val refreshState = notifications.loadState.refresh

    Scaffold(
        topBar = { Header(title = stringResource(id = R.string.inbox)) },
        bottomBar = {
            BottomBar()
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
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

            if(refreshState is LoadState.NotLoading && notifications.itemCount == 0) {
                MessageScreen(
                    message = stringResource(R.string.dontHaveAnyNotification),
                    icon = painterResource(R.drawable.ic_notifications_alert_outline)
                )
            }
        }
    }
}
package com.example.scrollbooker.feature.notifications.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.core.util.EmptyScreen
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.feature.notifications.components.NotificationsList
import com.example.scrollbooker.ui.theme.Background
import timber.log.Timber

@Composable
fun InboxScreen(viewModel: InboxViewModel) {
//    val notifications = viewModel.notifications.collectAsLazyPagingItems()
//    val refreshState = notifications.loadState.refresh
//
//    when(refreshState) {
//        is LoadState.Loading -> { LoadingScreen() }
//        is LoadState.Error -> {
//            val error = refreshState.error
//            Timber.tag("Notifications").e("ERROR: on Fetching Load Notifications $error")
//            ErrorScreen()
//        }
//        is LoadState.NotLoading -> {
//            if(notifications.itemCount == 0) {
//                EmptyScreen()
//            } else {
//                NotificationsList(notifications)
//            }
//        }
//    }
//
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .background(Background)
//        .statusBarsPadding()
//    ) {
//        Header(
//            title = stringResource(id = R.string.inbox),
//            enableBack = false,
//            navController = navController
//        )
//
//        when(refreshState) {
//            is LoadState.Loading -> { LoadingScreen() }
//            is LoadState.Error -> {
//                val error = refreshState.error
//                Timber.tag("Notifications").e("ERROR: on Fetching Load Notifications $error")
//                ErrorScreen()
//            }
//            is LoadState.NotLoading -> {
//                if(notifications.itemCount == 0) {
//                    EmptyScreen()
//                } else {
//                    NotificationsList(notifications)
//                }
//            }
//        }
//    }
}
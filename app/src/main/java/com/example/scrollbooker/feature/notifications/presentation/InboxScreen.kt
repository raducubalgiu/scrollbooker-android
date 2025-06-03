package com.example.scrollbooker.feature.notifications.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.ui.theme.Background

@Composable
fun InboxScreen(navController: NavController) {
    val viewModel: InboxViewModel = hiltViewModel()
    val notifications = viewModel.notifications.collectAsLazyPagingItems()

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
        ) {
            Header(
                title = stringResource(id = R.string.inbox),
                enableBack = false,
                navController = navController
            )

            LazyColumn {
                items(
                    count = notifications.itemCount,
                    key = { index -> notifications[index]?.id ?: index }
                ) { index ->
                    val notification = notifications[index]
                    notification?.let { Box(Modifier.fillMaxSize()) { Text("Notification") } }
                }

                notifications.apply {
                    when (loadState.refresh) {
                        is LoadState.Loading -> { /* Show Loading */ }
                        is LoadState.Error -> { /* Show Error */ }
                        else -> Unit
                    }

                    when (loadState.append) {
                        is LoadState.Loading -> { /* Show Loading */ }
                        is LoadState.Error -> { /* Show Error */ }
                        else -> Unit
                    }
                }
            }
        }
    }
}
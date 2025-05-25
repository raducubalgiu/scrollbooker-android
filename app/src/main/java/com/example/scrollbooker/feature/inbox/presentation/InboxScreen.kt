package com.example.scrollbooker.feature.inbox.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header
import com.example.scrollbooker.core.util.Dimens.BasePadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        Header(
            title = stringResource(id = R.string.inbox),
            enableBack = false,
            navController = navController
        )

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                coroutineScope.launch {
                    delay(200)
                    isRefreshing = false
                }
            },
            modifier = modifier
        ) {
            Spacer(Modifier.height(BasePadding))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(30) { item ->
                    NotificationItem(
                        url = "",
                        fullName = "Raducu Balgiu",
                        type = "follow",
                        isFollow = false
                    )
                }
            }
        }
    }
}
package com.example.scrollbooker.feature.appointments.presentation.tabs.business

import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.scrollbooker.feature.appointments.presentation.components.AppointmentClientCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsBusinessTab(modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

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

//        LazyColumn(
//            Modifier.fillMaxHeight()
//        ) {
//            items(40) { appointment ->
//                AppointmentClientCard(appointment = appointment)
//            }
//        }
    }
}
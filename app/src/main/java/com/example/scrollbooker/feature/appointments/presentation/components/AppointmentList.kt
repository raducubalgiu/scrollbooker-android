package com.example.scrollbooker.feature.appointments.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.ErrorScreen
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.core.util.MessageScreen
import com.example.scrollbooker.feature.appointments.domain.model.Appointment

@Composable
fun AppointmentsList(pagingItems: LazyPagingItems<Appointment>) {
    pagingItems.apply {
        when(loadState.refresh) {
            is LoadState.Loading -> LoadingScreen()
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> {
                if(pagingItems.itemCount == 0) {
                    MessageScreen(
                        message = stringResource(R.string.dontHaveAppointmentsYet),
                        icon = Icons.Outlined.Book
                    )
                }
            }
        }
    }

    LazyColumn(Modifier.fillMaxSize()) {
        items(pagingItems.itemCount) { index ->
            pagingItems[index]?.let { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    onAppointmentDetails = {}
                )
            }
        }

        pagingItems.apply {
            when (loadState.append) {
                is LoadState.Loading -> {
                    item { LoadMoreSpinner() }
                }

                is LoadState.Error -> {
                    item { Text("Ceva nu a mers cum trebuie") }
                }

                is LoadState.NotLoading -> Unit
            }
        }
    }
}
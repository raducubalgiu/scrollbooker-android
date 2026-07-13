package com.example.scrollbooker.ui.appointments
import BottomBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.scrollbooker.ui.appointments.components.AppointmentsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel,
    onNavigateToAppointmentDetails: (appointmentId: Int) -> Unit
) {
    val appointments = viewModel.appointments.collectAsLazyPagingItems()
    val isRefreshing = appointments.loadState.refresh is LoadState.Loading

    val refreshState = appointments.loadState.refresh
    val isInitialLoading = refreshState is LoadState.Loading && appointments.itemCount == 0

    Scaffold(
        topBar = { Header(title = stringResource(R.string.bookings)) },
        bottomBar = { BottomBar() }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Column(Modifier.fillMaxSize()) {
                when {
                    isInitialLoading -> LoadingScreen()
                    refreshState is LoadState.Error -> ErrorScreen()
                    else -> {
                        if(appointments.itemCount > 0) {
                            AppointmentsList(
                                appointments = appointments,
                                onNavigateToAppointmentDetails = onNavigateToAppointmentDetails,
                                isRefreshing = isRefreshing,
                                onRefresh = {
                                    viewModel.loadAppointments()
                                }
                            )
                        }
                    }
                }
            }

            if(appointments.itemCount == 0 && refreshState is LoadState.NotLoading) {
                MessageScreen(
                    message = stringResource(R.string.dontHaveAppointmentsYet),
                    icon = painterResource(R.drawable.ic_calendar_outline)
                )
            }
        }
    }
}

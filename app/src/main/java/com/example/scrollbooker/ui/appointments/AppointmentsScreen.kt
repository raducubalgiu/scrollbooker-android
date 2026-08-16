package com.example.scrollbooker.ui.appointments
import BottomBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.navigation.navigators.AppointmentsNavigator
import com.example.scrollbooker.ui.LocalBottomBarController
import com.example.scrollbooker.ui.appointments.components.AppointmentsList
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel,
    appointmentsNavigate: AppointmentsNavigator
) {
    val appointments = viewModel.appointments.collectAsLazyPagingItems()
    val isRefreshing = appointments.loadState.refresh is LoadState.Loading

    val refreshState = appointments.loadState.refresh
    val isInitialLoading = refreshState is LoadState.Loading && appointments.itemCount == 0

    val bottomBarController = LocalBottomBarController.current
    val newAppointments by bottomBarController.newCreatedAppointments.collectAsStateWithLifecycle()

    LaunchedEffect(isRefreshing) {
        if (isRefreshing && appointments.itemCount > 0 && newAppointments.isNotEmpty()) {
            bottomBarController.clearNewCreatedAppointments()
        }
    }

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
                        if (appointments.itemCount > 0 || newAppointments.isNotEmpty()) {
                            AppointmentsList(
                                newAppointments = newAppointments,
                                appointments = appointments,
                                onNavigateToAppointmentDetails = { appointmentsNavigate.toAppointmentDetails(it) },
                                isRefreshing = isRefreshing,
                                onRefresh = { viewModel.loadAppointments() },
                            )
                        }
                    }
                }
            }

            if(appointments.itemCount == 0 && newAppointments.isEmpty() && refreshState is LoadState.NotLoading) {
                MessageScreen(
                    message = stringResource(R.string.dontHaveAppointmentsYet),
                    icon = painterResource(R.drawable.ic_calendar_outline)
                )
            }
        }
    }
}

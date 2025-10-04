package com.example.scrollbooker.ui.appointments
import BottomBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.ArrowButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.enums.has
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.navigation.bottomBar.MainTab
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.ui.appointments.components.AppointmentFilter
import com.example.scrollbooker.ui.appointments.components.AppointmentFilterTitleEnum
import com.example.scrollbooker.ui.appointments.components.AppointmentsFilterSheet
import com.example.scrollbooker.ui.appointments.components.AppointmentsList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel,
    onNavigateToAppointmentDetails: (Appointment) -> Unit,
    appointmentsNumber: Int,
    onChangeTab: (MainTab) -> Unit
) {
    val appointments = viewModel.appointments.collectAsLazyPagingItems()
    val permissionsState by viewModel.permissionsState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedFilter by remember { mutableStateOf<AppointmentFilter?>(
        AppointmentFilter(title = AppointmentFilterTitleEnum.ALL, asCustomer = null)
    ) }

    var selectedOption by remember { mutableStateOf<AppointmentFilter?>(
        AppointmentFilter(title = AppointmentFilterTitleEnum.ALL, asCustomer = null)
    ) }

    if(sheetState.isVisible) {
        AppointmentsFilterSheet(
            sheetState = sheetState,
            selectedOption = selectedOption,
            onChange = { selectedOption = it },
            onCloseSheet =  {
                scope.launch {
                    if(selectedFilter != selectedOption) {
                        selectedOption = selectedFilter
                    }
                    sheetState.hide()
                }
            },
            onFilter = {
                scope.launch {
                    selectedFilter = selectedOption
                    sheetState.hide()
                    viewModel.loadAppointments(selectedFilter?.asCustomer)
                }
            },
        )
    }

    Scaffold(
        topBar = { Header(title = stringResource(R.string.appointments)) },
        bottomBar = {
            BottomBar(
                appointmentsNumber = appointmentsNumber,
                currentTab = MainTab.Appointments,
                currentRoute = MainRoute.Appointments.route,
                onChangeTab = onChangeTab
            )
        }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Column(Modifier.fillMaxSize()) {
                when(permissionsState) {
                    is FeatureState.Success -> {
                        val permissions = (permissionsState as FeatureState.Success).data

                        if(permissions.has(PermissionEnum.FILTER_APPOINTMENTS_VIEW)) {
                            ArrowButton(
                                title = selectedFilter?.title?.getLabel() ?: AppointmentFilterTitleEnum.ALL.getLabel(),
                                onClick = {
                                    scope.launch {
                                        sheetState.show()
                                    }
                                }
                            )
                        }
                    }
                    else -> Unit
                }

                Spacer(Modifier.height(BasePadding))

                when(appointments.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.NotLoading -> {
                        AppointmentsList(
                            appointments = appointments,
                            onNavigateToAppointmentDetails = onNavigateToAppointmentDetails
                        )
                    }
                }
            }

            Box {
                when(appointments.loadState.refresh) {
                    is LoadState.NotLoading -> {
                        if(appointments.itemCount == 0) {
                            MessageScreen(
                                message = stringResource(R.string.dontHaveAppointmentsYet),
                                icon = painterResource(R.drawable.ic_calendar_outline)
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}

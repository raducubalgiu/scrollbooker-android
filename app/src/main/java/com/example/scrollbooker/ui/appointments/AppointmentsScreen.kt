package com.example.scrollbooker.ui.appointments
import BottomBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.scrollbooker.components.customized.Protected.Protected
import com.example.scrollbooker.core.enums.PermissionEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.ui.appointments.components.AppointmentFilter
import com.example.scrollbooker.ui.appointments.components.AppointmentFilterTitleEnum
import com.example.scrollbooker.ui.appointments.components.AppointmentsFilterSheet
import com.example.scrollbooker.ui.appointments.components.AppointmentsList
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel,
    onNavigateToAppointmentDetails: (Appointment) -> Unit
) {
    val appointments = viewModel.appointments.collectAsLazyPagingItems()

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    var selectedFilter by remember { mutableStateOf<AppointmentFilter?>(
        AppointmentFilter(title = AppointmentFilterTitleEnum.ALL, asCustomer = null)
    ) }

    var selectedOption by remember { mutableStateOf<AppointmentFilter?>(
        AppointmentFilter(title = AppointmentFilterTitleEnum.ALL, asCustomer = null)
    ) }

    val isRefreshing = appointments.loadState.refresh is LoadState.Loading

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
        bottomBar = { BottomBar() }
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            Column(Modifier.fillMaxSize()) {
                Protected(permission = PermissionEnum.FILTER_APPOINTMENTS_VIEW) {
                    ArrowButton(
                        title = selectedFilter?.title?.getLabel() ?: AppointmentFilterTitleEnum.ALL.getLabel(),
                        onClick = { scope.launch { sheetState.show() } },
                        isFiltered = selectedFilter?.title?.key != AppointmentFilterTitleEnum.ALL.key,
                        onDeleteFilter = {
                            selectedFilter = AppointmentFilter(
                                title = AppointmentFilterTitleEnum.ALL,
                                asCustomer = null
                            )
                            selectedOption = AppointmentFilter(
                                title = AppointmentFilterTitleEnum.ALL,
                                asCustomer = null
                            )
                            viewModel.loadAppointments(selectedFilter?.asCustomer)
                        }
                    )

                    Spacer(Modifier.height(BasePadding))
                }

                when(appointments.loadState.refresh) {
                    is LoadState.Loading -> LoadingScreen()
                    is LoadState.Error -> ErrorScreen()
                    is LoadState.NotLoading -> {
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                viewModel.loadAppointments(selectedOption?.asCustomer)
                            },
                        ) {
                            AppointmentsList(
                                appointments = appointments,
                                onNavigateToAppointmentDetails = onNavigateToAppointmentDetails
                            )
                        }
                    }
                }
            }

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

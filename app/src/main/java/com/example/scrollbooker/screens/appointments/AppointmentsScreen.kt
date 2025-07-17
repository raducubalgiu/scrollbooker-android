package com.example.scrollbooker.screens.appointments
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.ArrowButton
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.components.core.layout.MessageScreen
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.LoadMoreSpinner
import com.example.scrollbooker.entity.appointment.domain.model.Appointment
import com.example.scrollbooker.screens.appointments.components.AppointmentCard.AppointmentCard
import com.example.scrollbooker.screens.appointments.components.AppointmentFilter
import com.example.scrollbooker.screens.appointments.components.AppointmentFilterTitleEnum
import com.example.scrollbooker.screens.appointments.components.AppointmentsHeader
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel,
    navigateToAppointmentDetails: (Appointment) -> Unit
) {
    val appointments = viewModel.appointments.collectAsLazyPagingItems()

    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    val filters = listOf(
        AppointmentFilter(
            title = AppointmentFilterTitleEnum.ALL,
            asCustomer = null
        ),
        AppointmentFilter(
            title = AppointmentFilterTitleEnum.AS_EMPLOYEE,
            asCustomer = false
        ),
        AppointmentFilter(
            title = AppointmentFilterTitleEnum.AS_CLIENT,
            asCustomer = true
        ),
    )

    var selectedOption by remember { mutableStateOf<AppointmentFilter?>(
        AppointmentFilter(title = AppointmentFilterTitleEnum.ALL, asCustomer = null)
    ) }
    var selectedFilter by remember { mutableStateOf<AppointmentFilter?>(
        AppointmentFilter(title = AppointmentFilterTitleEnum.ALL, asCustomer = null)
    ) }

    if(sheetState.isVisible) {
        Sheet(
            sheetState = sheetState,
            onClose = {
                coroutineScope.launch {
                    if(selectedFilter != selectedOption) {
                        selectedOption = selectedFilter
                    }
                    sheetState.hide()
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(BasePadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.filterAppointments),
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            LazyColumn {
                itemsIndexed(filters) { index, filter ->
                    InputRadio(
                        selected = filter.title == selectedOption?.title,
                        headLine = filter.title.getLabel(),
                        onSelect = { selectedOption = filter },
                    )

                    if(index < filters.size) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = SpacingXL),
                            color = Divider,
                            thickness = 0.55.dp
                        )
                    }
                }


            }

            MainButton(
                modifier = Modifier.padding(
                    vertical = BasePadding,
                    horizontal = SpacingXXL
                ),
                title = stringResource(R.string.filter),
                onClick = {
                    coroutineScope.launch {
                        selectedFilter = selectedOption
                        sheetState.hide()
                        viewModel.loadAppointments(selectedFilter?.asCustomer)
                    }
                }
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        AppointmentsHeader()

        ArrowButton(
            title = selectedFilter?.title?.getLabel() ?: AppointmentFilterTitleEnum.ALL.getLabel(),
            onClick = {
                coroutineScope.launch {
                    sheetState.show()
                }
            }
        )

        Spacer(Modifier.height(BasePadding))

        when(appointments.loadState.refresh) {
            is LoadState.Loading -> LoadingScreen()
            is LoadState.Error -> ErrorScreen()
            is LoadState.NotLoading -> {
                LazyColumn(Modifier.fillMaxSize()) {
                    item { Spacer(Modifier.height(BasePadding)) }

                    items(appointments.itemCount) { index ->
                        appointments[index]?.let { appointment ->
                            AppointmentCard(
                                appointment = appointment,
                                navigateToAppointmentDetails = navigateToAppointmentDetails
                            )

                            if(index < appointments.itemCount - 1) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = BasePadding,
                                            vertical = SpacingXL
                                        ),
                                    color = Divider,
                                    thickness = 0.55.dp
                                )
                            }
                        }
                    }

                    item {
                        when (appointments.loadState.append) {
                            is LoadState.Loading -> LoadMoreSpinner()
                            is LoadState.Error -> Text("Ceva nu a mers cum trebuie")
                            is LoadState.NotLoading -> Unit
                        }

                        Spacer(Modifier.height(BasePadding))
                    }
                }
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

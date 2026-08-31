package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.components.customized.Refresh
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.ui.appointments.components.AppointmentCard.AppointmentCard
import com.example.scrollbooker.ui.theme.Divider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsList(
    newAppointments: Set<Appointment>,
    appointments: LazyPagingItems<Appointment>,
    onNavigateToAppointmentDetails: (appointmentId: Int) -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    val appendState = appointments.loadState.append
    val listState = rememberLazyListState()

    LaunchedEffect(newAppointments) {
        if (newAppointments.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Refresh(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(BasePadding),
        ) {
            items(
                count = newAppointments.size,
                key = { index -> "new_appointment_${newAppointments.elementAt(index).id}" }
            ) { index ->
                val newAppointment = newAppointments.elementAt(index)

                AppointmentCard(
                    appointment = newAppointment,
                    isNew = true,
                    navigateToAppointmentDetails = onNavigateToAppointmentDetails
                )

                Spacer(Modifier.padding(vertical = SpacingS))
            }

            items(
                count = appointments.itemCount,
                key = { index -> appointments[index]?.id ?: index }
            ) { index ->
                val appointment = appointments[index]

                appointment?.let { appointment ->
                    AppointmentCard(
                        appointment = appointment,
                        navigateToAppointmentDetails = onNavigateToAppointmentDetails,
                        isNew = false
                    )

                    if(index < appointments.itemCount - 1) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = BasePadding),
                            color = Divider,
                            thickness = 0.55.dp
                        )
                    }
                }
            }

            item {
                if(appendState is LoadState.Loading) LoadMoreSpinner()
            }
        }
    }
}

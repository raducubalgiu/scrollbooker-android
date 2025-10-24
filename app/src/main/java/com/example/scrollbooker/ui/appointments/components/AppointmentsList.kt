package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.components.customized.LoadMoreSpinner
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.ui.appointments.components.AppointmentCard.AppointmentCard
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun AppointmentsList(
    appointments: LazyPagingItems<Appointment>,
    onNavigateToAppointmentDetails: (Appointment) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(BasePadding)
    ) {
        items(appointments.itemCount) { index ->
            appointments[index]?.let { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    navigateToAppointmentDetails = onNavigateToAppointmentDetails
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
        }
    }
}
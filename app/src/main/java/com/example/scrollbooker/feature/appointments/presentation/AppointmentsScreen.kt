package com.example.scrollbooker.feature.appointments.presentation
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.feature.appointments.presentation.tabs.AppointmentsTabs
import com.example.scrollbooker.feature.appointments.presentation.tabs.business.AppointmentsBusinessTab
import com.example.scrollbooker.feature.appointments.presentation.tabs.client.AppointmentsClientTab

@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel
) {
    val isEmployee = true
    val isBusiness = false
    val isCustomer = false

    Layout(
        headerTitle = stringResource(id = R.string.appointments),
        enableBack = false,
        enabledPadding = false
    ) {
        when {
            isEmployee -> AppointmentsTabs()
            isBusiness -> AppointmentsBusinessTab()
            isCustomer -> AppointmentsClientTab()
        }
    }
}

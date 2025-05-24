package com.example.scrollbooker.feature.appointments.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Header

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(navController: NavController) {
    val isEmployee = true
    val isBusiness = false
    val isCustomer = false

    Column {
        Header(
            navController = navController,
            enableBack = false,
            title = stringResource(id = R.string.appointments),
        )

        when {
            isEmployee -> AppointmentsTabs()
            isBusiness -> AppointmentsForOtherTab()
            isCustomer -> AppointmentsForMeTab()
        }
    }
}
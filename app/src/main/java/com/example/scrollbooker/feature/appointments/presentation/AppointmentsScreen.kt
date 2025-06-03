package com.example.scrollbooker.feature.appointments.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.feature.appointments.presentation.tabs.AppointmentsTabs
import com.example.scrollbooker.feature.appointments.presentation.tabs.business.AppointmentsBusinessTab
import com.example.scrollbooker.feature.appointments.presentation.tabs.client.AppointmentsClientTab
import com.example.scrollbooker.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(viewModel: AppointmentsViewModel) {
    val isEmployee = true
    val isBusiness = false
    val isCustomer = false

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Background)
        .statusBarsPadding()
    ) {
//        Header(
//            navController = navController,
//            enableBack = false,
//            title = stringResource(id = R.string.appointments),
//        )

        when {
            isEmployee -> AppointmentsTabs()
            isBusiness -> AppointmentsBusinessTab()
            isCustomer -> AppointmentsClientTab()
        }
    }
}
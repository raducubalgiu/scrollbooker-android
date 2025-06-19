package com.example.scrollbooker.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.ui.theme.Background

@Composable
fun AppointmentDetailsScreen(viewModel: AppointmentsViewModel) {
    Column(Modifier.fillMaxSize().background(Background)) {
        Header(
            onBack = {},
            title = "Detalii programare"
        )
    }
}
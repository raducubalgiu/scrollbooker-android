package com.example.scrollbooker.screens.profile.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun AppointmentConfirmationScreen(
    onBack: () -> Unit
) {
    Layout(
        headerTitle = "Confirma programarea",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {  }
            MainButton(
                title = "Confirma",
                onClick = {}
            )
        }
    }
}
package com.example.scrollbooker.feature.calendar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.calendar),
        onBack = onBack
    ) {  }
}
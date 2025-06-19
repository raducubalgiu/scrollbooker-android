package com.example.scrollbooker.screens.profile.myBusiness.myCalendar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.Layout

@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel,
    onBack: () -> Unit
) {
    Layout(
        headerTitle = stringResource(R.string.calendar),
        onBack = onBack
    ) {

    }
}
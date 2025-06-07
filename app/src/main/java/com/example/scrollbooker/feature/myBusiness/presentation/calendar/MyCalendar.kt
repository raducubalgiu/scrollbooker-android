package com.example.scrollbooker.feature.myBusiness.presentation.calendar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.feature.myBusiness.presentation.calendar.MyCalendarViewModel

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
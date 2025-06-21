package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessSchedules

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout

@Composable
fun CollectBusinessSchedulesScreen(
    viewModel: CollectBusinessSchedulesViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    FormLayout(
        headLine = stringResource(id = R.string.schedule),
        subHeadLine = stringResource(id = R.string.addYourBusinessSchedule),
        buttonTitle = stringResource(id = R.string.nextStep),
        onBack = onBack,
        onNext = onNext,
    ) {
        Text(text = "Business Schedules Screen")
    }
}
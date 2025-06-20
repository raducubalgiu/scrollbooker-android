package com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessSchedules

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.CollectBusinessDetails

@Composable
fun CollectBusinessSchedulesScreen(
    viewModel: CollectBusinessSchedulesViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    CollectBusinessDetails(
        isLastScreen = true,
        headLine = stringResource(id = R.string.schedule),
        subHeadLine = stringResource(id = R.string.addYourBusinessSchedule),
        onBack = onBack,
        onNext = onNext,
    ) {
        Text(text = "Business Schedules Screen")
    }
}
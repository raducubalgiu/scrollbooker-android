package com.example.scrollbooker.ui.onboarding.business
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.scrollbooker.ui.myBusiness.mySchedules.components.MySchedulesSection

@Composable
fun CollectBusinessSchedulesScreen(
    viewModel: CollectBusinessSchedulesViewModel,
    onNext: () -> Unit
) {
    val state by viewModel.schedulesState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    MySchedulesSection(
        state = state,
        isSaving = isSaving,
        onUpdateRow = { schedule, start, end ->
            viewModel.updateScheduleTime(
                schedule.copy(startTime = start, endTime = end)
            )
        },
        onHandleSave = onNext,
        onBack = {}
    )
}
package com.example.scrollbooker.ui.myBusiness.mySchedules
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.scrollbooker.ui.myBusiness.mySchedules.components.MySchedulesSection

@Composable
fun MySchedulesScreen(
    viewModel: MySchedulesViewModel,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit
) {
    val state by viewModel.schedulesState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    MySchedulesSection(
        state = state,
        isSaving = isSaving,
        onUpdateRow = { schedule, start, end ->
            val startTime = if(start == "null") null else start
            val endTime = if(end == "null") null else end

            viewModel.updateScheduleTime(
                schedule.copy(startTime = startTime, endTime = endTime)
            )
        },
        onHandleSave = onNextOrSave,
        onBack = onBack
    )
}
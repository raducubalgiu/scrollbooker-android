package com.example.scrollbooker.ui.myBusiness.mySchedules
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.scrollbooker.core.snackbar.CustomSnackBar
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.ui.myBusiness.mySchedules.components.MySchedulesSection

@Composable
fun MySchedulesScreen(
    viewModel: MySchedulesViewModel,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit
) {
    val state by viewModel.schedulesState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            snackBarController.show(event)
        }
    }

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
        onBack = onBack,
        snackBarHost = {
            CustomSnackBar(
                hostState = hostState,
                type = snackBarController.currentType
            )
        }
    )
}
package com.example.scrollbooker.feature.schedules.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.core.MainButton
import com.example.scrollbooker.core.snackbar.SnackbarManager
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.feature.schedules.domain.model.Schedule
import com.example.scrollbooker.feature.schedules.presentation.components.SchedulesList

@Composable
fun SchedulesScreen(
    viewModel: SchedulesViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.schedulesState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    Layout(
        headerTitle = stringResource(R.string.schedule),
        onBack = onBack
    ) {
        Column(modifier = Modifier
            .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                when(state) {
                    is FeatureState.Loading -> LoadingScreen()
                    is FeatureState.Error -> SnackbarManager.showToast(stringResource(id = R.string.somethingWentWrong))
                    is FeatureState.Success -> {
                        val schedules = (state as FeatureState.Success<List<Schedule>>).data

                        SchedulesList(
                            schedules,
                            onScheduleChange = { schedule ->
                                viewModel.updateScheduleTime(schedule)
                            }
                        )
                    }
                }
            }

            MainButton(
                isLoading = isSaving,
                onClick = { viewModel.updateSchedules() },
                title = stringResource(id = R.string.save),
                enabled = !isSaving
            )
        }
   }
}
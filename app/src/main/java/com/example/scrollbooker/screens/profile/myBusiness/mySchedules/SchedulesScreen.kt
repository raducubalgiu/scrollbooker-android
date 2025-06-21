package com.example.scrollbooker.screens.profile.myBusiness.mySchedules
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.core.snackbar.SnackbarManager
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.components.SchedulesList
import com.example.scrollbooker.shared.schedule.domain.model.Schedule

@Composable
fun SchedulesScreen(
    viewModel: SchedulesViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.schedulesState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    FormLayout(
        headLine = stringResource(R.string.schedule),
        subHeadLine = stringResource(R.string.scheduleSubheaderDescription),
        isEnabled = isSaving,
        buttonTitle = "Save",
        onBack = onBack,
        onNext = { viewModel.updateSchedules() }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = BasePadding,
                horizontal = SpacingXXL
            )
        ) {
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
    }
}
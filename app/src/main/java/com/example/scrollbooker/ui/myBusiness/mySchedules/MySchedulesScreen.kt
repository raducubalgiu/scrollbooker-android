package com.example.scrollbooker.ui.myBusiness.mySchedules
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.ui.myBusiness.mySchedules.components.ScheduleRow

@Composable
fun MySchedulesScreen(
    viewModel: MySchedulesViewModel,
    onBack: () -> Unit,
    onNextOrSave: () -> Unit
) {
    val state by viewModel.schedulesState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    FormLayout(
        headLine = stringResource(R.string.schedule),
        subHeadLine = stringResource(R.string.scheduleSubheaderDescription),
        buttonTitle = stringResource(R.string.save),
        isEnabled = isSaving != FeatureState.Loading,
        isLoading = isSaving is FeatureState.Loading,
        onBack = onBack,
        onNext = onNextOrSave
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = SpacingXXL
            )
        ) {
            when(val schedules = state) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> SnackbarManager.showToast(stringResource(id = R.string.somethingWentWrong))
                is FeatureState.Success -> {
                    LazyColumn {
                        items(schedules.data) { schedule ->
                            ScheduleRow(schedule,
                                onChange = { start, end ->
                                    viewModel.updateScheduleTime(
                                        schedule.copy(startTime = start, endTime = end)
                                    )
                                }
                            )
                            Spacer(Modifier.height(BasePadding))
                        }
                    }
                }
            }
        }
    }
}
package com.example.scrollbooker.feature.myBusiness.presentation.schedules
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.Header
import com.example.scrollbooker.components.core.Layout
import com.example.scrollbooker.components.core.MainButton
import com.example.scrollbooker.core.snackbar.SnackbarManager
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.LoadingScreen
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.feature.myBusiness.presentation.components.SchedulesList

@Composable
fun SchedulesScreen(navController: NavController) {
    val viewModel: SchedulesViewModel = hiltViewModel()
    val state by viewModel.schedulesState.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    Layout {
//        Header(
//            navController = navController,
//            title = stringResource(R.string.mySchedule),
//        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = SpacingXL),
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
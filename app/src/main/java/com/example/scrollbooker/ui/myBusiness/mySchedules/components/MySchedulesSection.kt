package com.example.scrollbooker.ui.myBusiness.mySchedules.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.FormLayout
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule

@Composable
fun MySchedulesSection(
    state: FeatureState<List<Schedule>>,
    isSaving: Boolean,
    onUpdateRow: (Schedule, String?, String?) -> Unit,
    onHandleSave: () -> Unit,
    onBack: () -> Unit,
    snackBarHost: @Composable (() -> Unit) = {},
) {
    var showErrors by rememberSaveable { mutableStateOf(false) }
    val schedules = (state as? FeatureState.Success)?.data

    val isFormValid by remember(schedules) {
        derivedStateOf {
            schedules?.all { isScheduleValid(it.startTime, it.endTime) }
        }
    }

    val invalidScheduleIds by remember(schedules) {
        derivedStateOf {
            schedules
                ?.filter { !isScheduleValid(it.startTime, it.endTime) }
                ?.map { it.id }
                ?.toSet()
        }
    }

    FormLayout(
        headLine = stringResource(R.string.schedule),
        subHeadLine = stringResource(R.string.scheduleSubheaderDescription),
        buttonTitle = stringResource(R.string.save),
        isEnabled = !isSaving,
        isLoading = isSaving,
        onBack = onBack,
        onNext = {
            showErrors = true

            if(isFormValid == true) {
                onHandleSave()
            }
        },
        snackBarHost = snackBarHost
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SpacingXXL)
        ) {
            when(val schedules = state) {
                is FeatureState.Loading -> LoadingScreen()
                is FeatureState.Error -> ErrorScreen()
                is FeatureState.Success -> {
                    LazyColumn {
                        items(schedules.data) { schedule ->
                            ScheduleRow(
                                schedule = schedule,
                                onChange = { start, end -> onUpdateRow(schedule, start, end) },
                                isNotValid = invalidScheduleIds?.contains(schedule.id) == true,
                                showErrors = showErrors
                            )

                            Spacer(Modifier.height(BasePadding))
                        }
                    }
                }
            }
        }
    }
}
package com.example.scrollbooker.ui.myBusiness.myBusinessDetails.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.components.core.layout.LoadingScreen
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.ui.myBusiness.mySchedules.components.ScheduleRow
import com.example.scrollbooker.ui.myBusiness.mySchedules.components.isScheduleValid

@Composable
fun MyBusinessSchedulesTab(
    schedulesState: FeatureState<List<Schedule>>,
    isSaving: Boolean,
    onUpdateRow: (Schedule, String?, String?) -> Unit,
    onSaveSchedules: () -> Unit
) {
    var showErrors by rememberSaveable { mutableStateOf(false) }
    val schedules = (schedulesState as? FeatureState.Success)?.data

    val isFormValid by remember(schedules) {
        derivedStateOf {
            schedules?.all { isScheduleValid(it.startTime, it.endTime) } == true
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

    Column(Modifier.fillMaxSize()) {
        when(schedulesState) {
            is FeatureState.Loading -> LoadingScreen()
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(BasePadding)
                ) {
                    items(schedulesState.data) { schedule ->
                        ScheduleRow(
                            schedule = schedule,
                            onChange = { start, end -> onUpdateRow(schedule, start, end) },
                            isNotValid = invalidScheduleIds?.contains(schedule.id) == true,
                            showErrors = showErrors
                        )

                        Spacer(Modifier.height(BasePadding))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainButton(
                        modifier = Modifier.padding(BasePadding),
                        onClick = {
                            showErrors = true
                            if(isFormValid) onSaveSchedules()
                        },
                        isLoading = isSaving,
                        enabled = !isSaving,
                        title = stringResource(R.string.save)
                    )
                }
            }
        }
    }
}

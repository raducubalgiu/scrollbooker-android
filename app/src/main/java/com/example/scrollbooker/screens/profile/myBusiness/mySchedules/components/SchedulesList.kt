package com.example.scrollbooker.screens.profile.myBusiness.mySchedules.components
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.shared.schedules.domain.model.Schedule

@Composable
fun SchedulesList(schedules: List<Schedule>, onScheduleChange: (Schedule) -> Unit) {
    LazyColumn {
        items(schedules) { schedule ->
            ScheduleRow(
                schedule,
                onChange = { start, end ->
                    onScheduleChange(schedule.copy(startTime = start, endTime = end))
                }
            )
            Spacer(Modifier.height(BasePadding))
        }
    }
}
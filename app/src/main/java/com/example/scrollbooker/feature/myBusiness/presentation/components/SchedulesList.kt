package com.example.scrollbooker.feature.myBusiness.presentation.components
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule

@Composable
fun SchedulesList(schedules: List<Schedule>) {
    LazyColumn {
        items(schedules) { schedule ->
            ScheduleRow(schedule)
            Spacer(Modifier.height(BasePadding))
        }
    }
}
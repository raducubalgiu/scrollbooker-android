package com.example.scrollbooker.feature.profile.presentation.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.feature.myBusiness.schedules.domain.model.Schedule
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

val schedules = listOf<Schedule>(
    Schedule(
        id = 1,
        dayOfWeek = "Luni",
        startTime = "09:00",
        endTime = "18:00",
    ),
    Schedule(
        id = 2,
        dayOfWeek = "Marti",
        startTime = "09:00",
        endTime = "18:00",
    ),
    Schedule(
        id = 3,
        dayOfWeek = "Miercuri",
        startTime = "09:00",
        endTime = "18:00",
    ),
    Schedule(
        id = 4,
        dayOfWeek = "Luni",
        startTime = "09:00",
        endTime = "18:00",
    ),
    Schedule(
        id = 5,
        dayOfWeek = "Joi",
        startTime = "09:00",
        endTime = "18:00",
    ),
    Schedule(
        id = 6,
        dayOfWeek = "Sambata",
        startTime = null,
        endTime = null,
    ),
    Schedule(
        id = 7,
        dayOfWeek = "Duminica",
        startTime = null,
        endTime = null,
    ),
)

@Composable
fun UserScheduleSheet() {
    schedules.forEach { (_, dayOfWeek, startTime, endTime) ->
        val text = if (startTime.isNullOrBlank()) stringResource(R.string.closed) else "$startTime - $endTime"

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = BasePadding,
                    vertical = SpacingM
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                style = titleMedium,
                fontWeight = FontWeight.SemiBold,
                text = dayOfWeek,
                color = OnSurfaceBG
            )
            Text(text)
        }
    }
}
package com.example.scrollbooker.components.customized

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.formatTime
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.translateDayOfWeek
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.ui.profile.components.sheets.WorkScheduleStatus
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.TextStyle
import java.util.Locale
import kotlin.ranges.contains

@Composable
fun SchedulesSection(
    schedules: List<Schedule>
) {
    val today = LocalDate.now().dayOfWeek
    val todayName = today.getDisplayName(TextStyle.FULL, Locale.ENGLISH)

    fun getWorkScheduleStatus(startTime: String?, endTime: String?): WorkScheduleStatus {
        if(startTime == null || endTime == null) return WorkScheduleStatus.CLOSED

        val start = LocalTime.parse(startTime)
        val end = LocalTime.parse(endTime)

        val duration = Duration.between(start, end).toHours()

        return when {
            duration >= 8 -> WorkScheduleStatus.FULL
            duration in 1..7 -> WorkScheduleStatus.SHORT
            else -> WorkScheduleStatus.CLOSED
        }
    }

    schedules.forEach { (_, dayOfWeek, startTime, endTime) ->
        val text = if (startTime.isNullOrBlank()) stringResource(R.string.closed)
        else "${formatTime(startTime)} - ${formatTime(endTime)}"

        val isToday = dayOfWeek == todayName
        val schedulesStatus = getWorkScheduleStatus(startTime, endTime)

        val statusBg = when(schedulesStatus) {
            WorkScheduleStatus.CLOSED -> Color(0xFFCCCCCC)
            WorkScheduleStatus.SHORT -> Color(0xFFFBBF24)
            WorkScheduleStatus.FULL -> Color.Green
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SpacingXL),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row( verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(10.dp)
                        .height(10.dp)
                        .background(statusBg)
                )
                Spacer(Modifier.width(BasePadding))
                Text(
                    style = titleMedium,
                    fontWeight = if(isToday) FontWeight.ExtraBold else FontWeight.Normal,
                    fontSize = 18.sp,
                    text = translateDayOfWeek(dayOfWeek) ?: dayOfWeek,
                    color = OnBackground,
                )
            }
            Text(
                text = text,
                style = bodyLarge,
                color = OnBackground,
                fontWeight = if(isToday) FontWeight.ExtraBold else FontWeight.Normal,
            )
        }
    }
}
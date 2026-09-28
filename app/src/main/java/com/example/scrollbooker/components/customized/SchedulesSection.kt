package com.example.scrollbooker.components.customized

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.extensions.formatTime
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.translateDayOfWeek
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.ui.profile.sheets.WorkScheduleStatus
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import org.threeten.bp.DayOfWeek
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import kotlin.ranges.contains

@Composable
fun SchedulesSection(
    modifier: Modifier = Modifier,
    schedules: List<Schedule>
) {
    val today = LocalDate.now().dayOfWeek

    fun getWorkScheduleStatus(startTime: String?, endTime: String?): WorkScheduleStatus {
        if (startTime.isNullOrBlank() || endTime.isNullOrBlank()) return WorkScheduleStatus.CLOSED
        return try {
            val start = LocalTime.parse(startTime)
            val end = LocalTime.parse(endTime)
            val duration = Duration.between(start, end).toHours()
            when {
                duration >= 8 -> WorkScheduleStatus.FULL
                duration in 1..7 -> WorkScheduleStatus.SHORT
                else -> WorkScheduleStatus.CLOSED
            }
        } catch (e: Exception) {
            WorkScheduleStatus.CLOSED
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = ShapeDefaults.Large,
        colors = CardDefaults.cardColors(containerColor = SurfaceBG.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(BasePadding),
            verticalArrangement = Arrangement.spacedBy(SpacingM)
        ) {
            schedules.forEach { (_, dayOfWeekStr, startTime, endTime) ->
                val scheduleDay = try { DayOfWeek.valueOf(dayOfWeekStr.uppercase()) } catch(e: Exception) { null }
                val isToday = scheduleDay == today

                val timeText = if (startTime.isNullOrBlank()) stringResource(R.string.closed)
                else "${formatTime(startTime)} - ${formatTime(endTime)}"

                val scheduleStatus = getWorkScheduleStatus(startTime, endTime)

                val statusColor = when (scheduleStatus) {
                    WorkScheduleStatus.CLOSED -> OnBackground.copy(alpha = 0.3f)
                    WorkScheduleStatus.SHORT -> Color(0xFFFBBF24)
                    WorkScheduleStatus.FULL -> Color(0xFF10B981)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(ShapeDefaults.Medium)
                        .background(if (isToday) OnBackground.copy(alpha = 0.05f) else Color.Transparent)
                        .padding(horizontal = SpacingS, vertical = SpacingXS),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(statusColor, CircleShape)
                    )

                    Spacer(Modifier.width(SpacingM))

                    Text(
                        modifier = Modifier.weight(4f),
                        text = translateDayOfWeek(dayOfWeekStr) ?: dayOfWeekStr,
                        style = bodyMedium,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                        color = if (isToday) Primary else OnBackground,
                    )

                    Text(
                        modifier = Modifier.weight(5f),
                        text = timeText,
                        style = bodyMedium,
                        textAlign = TextAlign.End,
                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                        color = if (startTime.isNullOrBlank()) OnBackground.copy(alpha = 0.5f) else OnBackground
                    )
                }
            }
        }
    }
}

package com.example.scrollbooker.ui.profile.components.profileHeader.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.formatTime
import com.example.scrollbooker.core.util.translateDayOfWeek
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.ui.profile.myProfile.myBusiness.mySchedules.MySchedulesViewModel
import com.example.scrollbooker.ui.theme.bodyLarge
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.TextStyle
import java.util.Locale

enum class WorkScheduleStatus {
    CLOSED, SHORT, FULL
}

@Composable
fun UserScheduleSheet() {
    val viewModel: MySchedulesViewModel = hiltViewModel()
    val state by viewModel.schedulesState.collectAsState()

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

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = BasePadding)
    ) {
        when(state) {
            is FeatureState.Loading -> {
                val brush = rememberShimmerBrush()

                repeat(7) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(
                                horizontal = BasePadding,
                                vertical = SpacingM
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(Modifier
                            .fillMaxWidth(fraction = 0.4f)
                            .height(BasePadding)
                            .background(brush)
                        )
                        Spacer(Modifier
                            .fillMaxWidth(fraction = 0.2f)
                            .height(BasePadding)
                            .background(brush)
                        )
                    }
                }
            }
            is FeatureState.Error -> ErrorScreen()
            is FeatureState.Success -> {
                val schedules = (state as FeatureState.Success<List<Schedule>>).data

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
                            .padding(
                                horizontal = BasePadding,
                                vertical = SpacingM
                            ),
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
                                text = translateDayOfWeek(dayOfWeek) ?: dayOfWeek,
                                color = OnSurfaceBG,
                            )
                        }
                        Text(
                            text = text,
                            style = bodyLarge,
                            fontWeight = if(isToday) FontWeight.ExtraBold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}
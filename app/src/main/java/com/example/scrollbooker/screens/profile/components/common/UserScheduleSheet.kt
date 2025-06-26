package com.example.scrollbooker.screens.profile.components.common
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
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
import com.example.scrollbooker.components.core.shimmer.rememberShimmerBrush
import com.example.scrollbooker.core.util.Dimens.SpacingXXL
import com.example.scrollbooker.components.core.layout.ErrorScreen
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.core.util.formatTime
import com.example.scrollbooker.core.util.translateDayOfWeek
import com.example.scrollbooker.screens.profile.myBusiness.mySchedules.MySchedulesViewModel
import com.example.scrollbooker.entity.schedule.domain.model.Schedule

@Composable
fun UserScheduleSheet() {
    val viewModel: MySchedulesViewModel = hiltViewModel()
    val state by viewModel.schedulesState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SpacingXXL)
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
                            text = translateDayOfWeek(dayOfWeek) ?: dayOfWeek,
                            color = OnSurfaceBG
                        )
                        Text(text)
                    }
                }
            }
        }
    }
}
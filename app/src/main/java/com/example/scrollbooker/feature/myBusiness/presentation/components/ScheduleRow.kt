package com.example.scrollbooker.feature.myBusiness.presentation.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.inputs.InputSelect
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.formatTime
import com.example.scrollbooker.core.util.generateTimeSlots
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun ScheduleRow(schedule: Schedule) {
    var selectedStartTime by remember { mutableStateOf(formatTime(schedule.startTime ?: "Inchis")) }
    var selectedEndTime by remember { mutableStateOf(formatTime(schedule.endTime ?: "Inchis")) }
    val slots = generateTimeSlots()

    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(Modifier.width(90.dp)) {
            Text(
                style = titleMedium,
                fontWeight = FontWeight.SemiBold,
                text = schedule.dayOfWeek,
                color = OnBackground,
            )
        }

        Spacer(Modifier.width(BasePadding))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputSelect(
                modifier = Modifier.weight(1f),
                options = slots,
                selectedOption = selectedStartTime.toString(),
                onSetSelected = { selectedStartTime = it }
            )

            Spacer(Modifier.width(BasePadding))

            InputSelect(
                modifier = Modifier.weight(1f),
                options = slots,
                selectedOption = selectedEndTime.toString(),
                onSetSelected = { selectedEndTime = it }
            )
        }
    }
}
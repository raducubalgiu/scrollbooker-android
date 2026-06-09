package com.example.scrollbooker.ui.search.sheets.services.steps
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.LastMinute
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

enum class TimeIntervalPreset(
    val start: LocalTime?,
    val end: LocalTime?
) {
    ANYTIME(start = null, end = null),
    MORNING(start = LocalTime.of(9, 0), end = LocalTime.of(12, 0)),
    LUNCH(start = LocalTime.of(12, 0), end = LocalTime.of(18, 0)),
    EVENING(start = LocalTime.of(18, 0), end = LocalTime.of(22, 0)),
    CUSTOM(start = null, end = null)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeSection(
    startTime: LocalTime?,
    endTime: LocalTime?,
    onTimeChange: (LocalTime?, LocalTime?) -> Unit
) {
    val currentPreset = remember(startTime, endTime) {
        when {
            startTime == null && endTime == null -> TimeIntervalPreset.ANYTIME
            startTime == TimeIntervalPreset.MORNING.start && endTime == TimeIntervalPreset.MORNING.end -> TimeIntervalPreset.MORNING
            startTime == TimeIntervalPreset.LUNCH.start && endTime == TimeIntervalPreset.LUNCH.end -> TimeIntervalPreset.LUNCH
            startTime == TimeIntervalPreset.EVENING.start && endTime == TimeIntervalPreset.EVENING.end -> TimeIntervalPreset.EVENING
            else -> TimeIntervalPreset.CUSTOM
        }
    }

    var showPickerForStart by remember { mutableStateOf(false) }
    var showPickerForEnd by remember { mutableStateOf(false) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    Column {
        Text(
            text = stringResource(R.string.timeInterval),
            style = titleMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(horizontal = BasePadding)
        )

        Spacer(Modifier.height(BasePadding))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BasePadding)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(SpacingM)
        ) {
            TimeIntervalCard(
                hasTime = false,
                title = stringResource(R.string.anytime),
                isSelected = currentPreset == TimeIntervalPreset.ANYTIME,
                onClick = {
                    onTimeChange(
                        TimeIntervalPreset.ANYTIME.start,
                        TimeIntervalPreset.ANYTIME.end
                    )
                }
            )

            TimeIntervalCard(
                hasTime = true,
                title = stringResource(R.string.morning),
                description = "09:00 - 12:00",
                isSelected = currentPreset == TimeIntervalPreset.MORNING,
                onClick = {
                    onTimeChange(
                        TimeIntervalPreset.MORNING.start,
                        TimeIntervalPreset.MORNING.end
                    )
                }
            )

            TimeIntervalCard(
                hasTime = true,
                title = stringResource(R.string.afternoon),
                description = "12:00 - 18:00",
                isSelected = currentPreset == TimeIntervalPreset.LUNCH,
                onClick = {
                    onTimeChange(
                        TimeIntervalPreset.LUNCH.start,
                        TimeIntervalPreset.LUNCH.end
                    )
                }
            )

            TimeIntervalCard(
                hasTime = true,
                title = stringResource(R.string.evening),
                description = "18:00 - 22:00",
                isSelected = currentPreset == TimeIntervalPreset.EVENING,
                onClick = {
                    onTimeChange(
                        TimeIntervalPreset.EVENING.start,
                        TimeIntervalPreset.EVENING.end
                    )
                }
            )

            TimeIntervalCard(
                hasTime = false,
                title = "Custom",
                isSelected = currentPreset == TimeIntervalPreset.CUSTOM,
                onClick = {
                    val defaultStart = startTime ?: LocalTime.of(9, 0)
                    val defaultEnd = endTime ?: LocalTime.of(18, 0)

                    onTimeChange(defaultStart, defaultEnd)
                }
            )
        }

        AnimatedVisibility(visible = currentPreset == TimeIntervalPreset.CUSTOM) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BasePadding, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SpacingM)
                ) {

                    OutlinedTextField(
                        value = startTime?.format(timeFormatter) ?: "",
                        onValueChange = {},
                        label = { Text(stringResource(R.string.from)) },
                        readOnly = true,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showPickerForStart = true },
                        enabled = false,
                        shape = ShapeDefaults.Medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = OnBackground,
                            disabledBorderColor = Divider,
                            disabledLabelColor = Color.Gray,
                            focusedBorderColor = LastMinute,
                            unfocusedBorderColor = Divider
                        )
                    )

                    OutlinedTextField(
                        value = endTime?.format(timeFormatter) ?: "",
                        onValueChange = {},
                        label = { Text(stringResource(R.string.until)) },
                        readOnly = true,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { showPickerForEnd = true },
                        enabled = false,
                        shape = ShapeDefaults.Medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = OnBackground,
                            disabledBorderColor = Divider,
                            disabledLabelColor = Color.Gray,
                            focusedBorderColor = LastMinute,
                            unfocusedBorderColor = Divider
                        )
                    )
                }
            }
        }
    }

    if (showPickerForStart) {
        CustomHourPickerDialog(
            initialTime = startTime ?: LocalTime.of(9, 0),
            onDismiss = { showPickerForStart = false },
            onTimeSelected = { selectedTime ->
                onTimeChange(selectedTime, endTime)
                showPickerForStart = false
            },
            isTimeValid = { chosenStart -> endTime == null || chosenStart.isBefore(endTime) }
        )
    }

    if (showPickerForEnd) {
        CustomHourPickerDialog(
            initialTime = endTime ?: LocalTime.of(18, 0),
            onDismiss = { showPickerForEnd = false },
            onTimeSelected = { selectedTime ->
                onTimeChange(startTime, selectedTime)
                showPickerForEnd = false
            },
            isTimeValid = { chosenEnd -> startTime == null || chosenEnd.isAfter(startTime) }
        )
    }
}
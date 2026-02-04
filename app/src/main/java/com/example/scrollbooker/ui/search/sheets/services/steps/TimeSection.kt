package com.example.scrollbooker.ui.search.sheets.services.steps
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.generateTimeSlots
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.LocalTime

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

    Column {
        Text(
            text = stringResource(R.string.timeInterval),
            style = titleMedium,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(
                top = BasePadding,
                bottom = 8.dp,
                start = BasePadding,
                end = BasePadding
            )
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
                title = "Oricand",
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
                title = "Dimineata",
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
                title = "La pranz",
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
                title = "Seara",
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
                onClick = {}
            )
        }

        val slots = remember { generateTimeSlots() }

//        AnimatedVisibility(visible = selection.preset == TimePreset.CUSTOM) {
//            Spacer(Modifier.height(16.dp))
//            // Inputs from - to Here
//        }
    }
}

@Composable
private fun TimeIntervalCard(
    hasTime: Boolean,
    title: String,
    description: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .heightIn(min = 65.dp)
            .widthIn(min = 130.dp)
            .border(
                width = if(isSelected) 2.dp else 1.dp,
                color = if(isSelected) Primary else Divider,
                shape = ShapeDefaults.Medium
            )
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = titleMedium,
            fontWeight = FontWeight.SemiBold,
            text = title
        )
        if(hasTime && !description.isNullOrEmpty()) {
            Spacer(Modifier.height(SpacingXS))
            Text(
                text = description,
                color = Color.Gray
            )
        }
    }
}
package com.example.scrollbooker.ui.myBusiness.myCalendar.addOwnClient.sheets.selectDateTime
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.components.customized.calendar.CalendarHeaderState
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.availability.domain.model.AvailableDay
import com.example.scrollbooker.entity.booking.availability.domain.model.Slot
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

private enum class DateTimeStep { CALENDAR, SLOTS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerSheet(
    sheetState: SheetState,
    calendarHeaderState: FeatureState<CalendarHeaderState>,
    selectedDay: LocalDate?,
    daySlots: FeatureState<AvailableDay>?,
    startOnSlotsStep: Boolean,
    initialPendingSlotUtc: String?,
    onDayClick: (LocalDate) -> Unit,
    onConfirm: (Slot) -> Unit,
    onDismiss: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    var step by remember { mutableStateOf(if (startOnSlotsStep) DateTimeStep.SLOTS else DateTimeStep.CALENDAR) }
    var pendingSlotUtc by remember { mutableStateOf(initialPendingSlotUtc) }

    Scaffold(
        containerColor = Background,
        topBar = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (step == DateTimeStep.SLOTS) {
                        CustomIconButton(
                            boxSize = 60.dp,
                            imageVector = Icons.Filled.CalendarMonth,
                            onClick = {
                                pendingSlotUtc = null
                                step = DateTimeStep.CALENDAR
                            }
                        )
                    } else {
                        Box(Modifier.size(60.dp))
                    }

                    Text(
                        text = stringResource(R.string.selectDateAndTime),
                        style = titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    CustomIconButton(
                        boxSize = 60.dp,
                        imageVector = Icons.Default.Close,
                        onClick = onDismiss
                    )
                }

                HorizontalDivider(color = Divider, thickness = 0.55.dp)
            }
        },
        bottomBar = {
            if (step == DateTimeStep.SLOTS) {
                Column(Modifier.navigationBarsPadding()) {
                    HorizontalDivider(color = Divider, thickness = 0.55.dp)

                    MainButton(
                        modifier = Modifier.padding(BasePadding),
                        title = stringResource(R.string.confirm),
                        enabled = pendingSlotUtc != null,
                        onClick = {
                            val availableSlots = (daySlots as? FeatureState.Success)?.data?.availableSlots.orEmpty()
                            val slot = availableSlots.firstOrNull { it.startDateUtc == pendingSlotUtc }
                                ?: return@MainButton

                            scope.launch {
                                sheetState.hide()
                                onConfirm(slot)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    val forward = targetState.ordinal > initialState.ordinal
                    val slideOffset = { width: Int -> if (forward) width / 4 else -width / 4 }

                    (fadeIn(tween(220)) + slideInHorizontally(tween(220), initialOffsetX = slideOffset))
                        .togetherWith(fadeOut(tween(220)) + slideOutHorizontally(tween(220), targetOffsetX = { -slideOffset(it) }))
                },
                label = "DateTimeStepTransition"
            ) { targetStep ->
                when (targetStep) {
                    DateTimeStep.CALENDAR -> DateTimeCalendarView(
                        calendarHeaderState = calendarHeaderState,
                        onDayClick = { date ->
                            pendingSlotUtc = null
                            onDayClick(date)
                            step = DateTimeStep.SLOTS
                        }
                    )

                    DateTimeStep.SLOTS -> DateTimeSlotsView(
                        day = selectedDay,
                        daySlots = daySlots,
                        pendingSlotUtc = pendingSlotUtc,
                        onSlotSelected = { pendingSlotUtc = it.startDateUtc }
                    )
                }
            }
        }
    }
}

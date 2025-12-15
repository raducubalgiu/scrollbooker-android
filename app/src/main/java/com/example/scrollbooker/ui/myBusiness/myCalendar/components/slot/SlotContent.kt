package com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.isFreeSlot
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.labelMedium

@Composable
fun SlotContent(
    slot: CalendarEventsSlot,
    lineColor: Color,
    height: Dp,
    isBlocking: Boolean,
    isBlocked: Boolean,
    isBefore: Boolean,
    onIsBlocking: (Boolean) -> Unit,
    isPermanentlyBlocked: Boolean,
) {
    val isCompact = height < 40.dp
    val isVeryCompact = height < 28.dp

    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(Modifier.fillMaxWidth()) {
            if(!slot.isFreeSlot()) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .fillMaxHeight()
                        .clip(shape = ShapeDefaults.ExtraLarge)
                        .background(lineColor)
                )

                Spacer(Modifier.width(SpacingS))
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${slot.startDateLocale!!.toLocalTime()} - ${slot.endDateLocale!!.toLocalTime()}",
                        style = labelMedium,
                        maxLines = 1
                    )

                    if(slot.isFreeSlot() && isBlocking) {
                        Checkbox(
                            checked = isBlocked,
                            //enabled = isEnabled,
                            onCheckedChange = onIsBlocking,
                            colors = CheckboxColors(
                                checkedCheckmarkColor = Color.White,
                                uncheckedCheckmarkColor = Color.Transparent,
                                checkedBoxColor = Primary,
                                uncheckedBoxColor = Color.Transparent,
                                disabledCheckedBoxColor = Divider,
                                disabledUncheckedBoxColor = Divider,
                                disabledIndeterminateBoxColor = Divider,
                                checkedBorderColor = Primary,
                                uncheckedBorderColor = Color.Gray,
                                disabledBorderColor = Divider,
                                disabledUncheckedBorderColor = Divider,
                                disabledIndeterminateBorderColor = Divider
                            )
                        )
                    }
                }

                if (!isVeryCompact) {
                    val title = slot.info?.customer?.fullname ?: "Rezervat"
                    val maxLines = if (isCompact) 1 else 2

                    when {
                        slot.isBooked -> SlotIsBooked(title, maxLines)
                        slot.isLastMinute -> SlotIsLastMinute(title = "Last minute • ${slot.lastMinuteDiscount}%")
                        slot.isBlocked -> SlotIsBlocked(lineColor, message = "")
                        isBefore -> SlotIsBefore()
                        else -> SlotFree()
                    }
                }
            }
        }
    }
}
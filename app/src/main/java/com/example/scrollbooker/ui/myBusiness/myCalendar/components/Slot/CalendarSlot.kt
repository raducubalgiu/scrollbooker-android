package com.example.scrollbooker.ui.myBusiness.myCalendar.components.Slot
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Primary
import org.threeten.bp.LocalDateTime

@Composable
fun CalendarSlot(
    height: Dp,
    offsetY: Dp,
    slot: CalendarEventsSlot,
    isPermanentlyBlocked: Boolean,
    isBlocked: Boolean,
    isBlocking: Boolean,
    onBlock: (LocalDateTime) -> Unit,
    onSlotClick: (CalendarEventsSlot) -> Unit
) {
    fun onFreeSlotClick() {
        if(isBlocking) slot.startDateLocale?.let { onBlock(it) }
        else onSlotClick(slot)
    }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .offset(y = offsetY)
        .padding(horizontal = 8.dp, vertical = 4.dp)
        .clickable { onSlotClick(slot) },
        contentAlignment = Alignment.Center
    ) {

        when {
            slot.isBooked -> CalendarBookedSlot(slot = slot)
            isPermanentlyBlocked -> CalendarBlockedSlot(slot.info?.message)
            else -> {
                CalendarFreeSlot(
                    onClick = { onFreeSlotClick() },
                    isBlocking = isBlocking,
                    isBlocked = isBlocked
                )
            }
        }
    }
}

@Composable
fun SlotCheckbox(
    isBlocked: Boolean,
    isEnabled: Boolean,
    onCheckedChange: (() -> Unit)? = null
) {
    Box(Modifier.fillMaxSize()) {
        Checkbox(
            modifier = Modifier.align(Alignment.TopEnd),
            checked = isBlocked,
            enabled = isEnabled,
            onCheckedChange = { onCheckedChange?.invoke() },
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
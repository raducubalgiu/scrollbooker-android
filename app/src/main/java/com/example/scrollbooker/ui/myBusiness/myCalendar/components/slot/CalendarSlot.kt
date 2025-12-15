package com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.entity.booking.calendar.domain.model.SlotUiStyle

@Composable
fun CalendarSlot(
    height: Dp,
    offsetY: Dp,
    slot: CalendarEventsSlot,
    style: SlotUiStyle,
    isBlocked: Boolean,
    isPermanentlyBlocked: Boolean,
    isBlocking: Boolean,
    onIsBlocking: (Boolean) -> Unit,
    onSlotClick: (CalendarEventsSlot) -> Unit,
    minTouchHeight: Dp = 44.dp
) {
    val visualHeight = height
    val touchHeight = androidx.compose.ui.unit.max(height, minTouchHeight)
    val isEnabled = style.isEnabled
    val isBefore = style.isBefore

    Box(
        modifier = Modifier
            .offset(y = offsetY)
            .height(touchHeight)
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(shape = ShapeDefaults.Medium)
            .background(style.background)
            .border(
                width = 1.dp,
                color = style.borderColor,
                shape = ShapeDefaults.Medium
            )
            .clickable(
                enabled = isEnabled && !isBlocking
            ) {
                onSlotClick(slot)
            }
    ) {
        Box(
            modifier = Modifier
                .height(visualHeight)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            SlotContent(
                slot = slot,
                height = visualHeight,
                isBlocking = isBlocking,
                isBlocked = isBlocked,
                onIsBlocking = onIsBlocking,
                isBefore = isBefore,
                isPermanentlyBlocked = isPermanentlyBlocked,
                lineColor = style.lineColor
            )
        }
    }
}
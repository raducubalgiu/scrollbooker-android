package com.example.scrollbooker.ui.myBusiness.myCalendar.components
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.enums.AppointmentChannelEnum
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun CalendarSlot(
    height: Dp,
    offsetY: Dp,
    slot: CalendarEventsSlot,
    onSlotClick: (CalendarEventsSlot) -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .offset(y = offsetY)
        .padding(horizontal = 8.dp, vertical = 4.dp)
        .clickable { onSlotClick(slot) },
        contentAlignment = Alignment.Center
    ) {
        val isScrollBooker = slot.info?.channel == AppointmentChannelEnum.SCROLL_BOOKER
        val isOwnClient = slot.info?.channel == AppointmentChannelEnum.OWN_CLIENT

        when {
            slot.isBooked && isScrollBooker -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Primary)
                ) {  }
            }

            slot.isBooked && isOwnClient -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Green)
                ) {  }
            }

            slot.isBlocked -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Error)
                ) {  }
            }
            else -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(SurfaceBG)
                    .clickable { onSlotClick(slot) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(R.drawable.ic_circle_plus_outline),
                        contentDescription = "Add Icon",
                        tint = Primary
                    )
                }
            }
        }
    }
}
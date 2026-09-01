package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot.CalendarSlotShimmer
import com.example.scrollbooker.ui.myBusiness.myCalendar.util.rememberHourHeight

private const val PLACEHOLDER_COUNT = 5

@Composable
fun DayTimelineShimmer(slotDuration: Int) {
    val hourHeight = rememberHourHeight(slotDuration)
    val dpPerMinute = hourHeight / 60f
    val slotHeight = dpPerMinute * slotDuration - 6.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(PLACEHOLDER_COUNT) {
            CalendarSlotShimmer(height = slotHeight)
        }
    }
}

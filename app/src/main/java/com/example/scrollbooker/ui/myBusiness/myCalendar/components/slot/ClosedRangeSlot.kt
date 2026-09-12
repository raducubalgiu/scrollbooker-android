package com.example.scrollbooker.ui.myBusiness.myCalendar.components.slot
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.labelMedium

// Non-interactive placeholder for hours a specific employee doesn't work, within the wider
// business-day range shown on the timeline (see MyCalendarPagerSection.businessDayWindow) -
// styled like a blocked slot (same Error-based background/border), never clickable.
@Composable
fun ClosedRangeSlot(
    height: Dp,
    offsetY: Dp
) {
    Box(
        modifier = Modifier
            .offset(y = offsetY)
            .height(height)
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clip(shape = ShapeDefaults.Medium)
            .background(Error.copy(alpha = 0.14f))
            .border(
                width = 1.dp,
                color = Error.copy(alpha = 0.45f),
                shape = ShapeDefaults.Medium
            ),
        contentAlignment = Alignment.Center
    ) {
        if (height > 28.dp) {
            Text(
                text = stringResource(R.string.closed),
                style = labelMedium,
                color = Error
            )
        }
    }
}

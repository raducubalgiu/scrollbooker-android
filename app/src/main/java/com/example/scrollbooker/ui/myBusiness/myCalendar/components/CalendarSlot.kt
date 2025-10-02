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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.entity.booking.calendar.domain.model.CalendarEventsSlot
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import org.threeten.bp.LocalDateTime

@Composable
fun CalendarSlot(
    height: Dp,
    offsetY: Dp,
    slot: CalendarEventsSlot,
    isBlocked: Boolean,
    isBlocking: Boolean,
    onBlock: (LocalDateTime) -> Unit,
    onSlotClick: (CalendarEventsSlot) -> Unit
) {
    fun handleCheckbox(startDateLocale: LocalDateTime) {
        onBlock(startDateLocale)
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
            slot.isBooked -> CalendarBookedSlot(slot=slot)

            isBlocked -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Error.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = slot.info?.message ?: stringResource(R.string.blocked),
                        color = Color.Red
                    )
                }

                SlotCheckbox(
                    isBlocked = isBlocked,
                    isEnabled = false,
                    onCheckedChange = {
                        slot.startDateLocale?.let { handleCheckbox(slot.startDateLocale) }
                    },
                )
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

                    if(isBlocking) {
                        SlotCheckbox(
                            isBlocked = isBlocked,
                            isEnabled = true,
                            onCheckedChange = {
                                slot.startDateLocale?.let { handleCheckbox(slot.startDateLocale) }
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SlotCheckbox(
    isBlocked: Boolean,
    isEnabled: Boolean,
    onCheckedChange: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Checkbox(
            modifier = Modifier.align(Alignment.TopEnd),
            checked = isBlocked,
            enabled = isEnabled,
            onCheckedChange = { onCheckedChange() },
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
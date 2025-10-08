package com.example.scrollbooker.ui.myBusiness.myCalendar.components.Slot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG
import org.threeten.bp.LocalDateTime

@Composable
fun CalendarFreeSlot(
    startDateLocale: LocalDateTime?,
    onSlotClick: () -> Unit,
    isBlocking: Boolean,
    isBlocked: Boolean,
) {
    val now = LocalDateTime.now()
    val isBefore = startDateLocale?.isBefore(now) != false

    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(if(isBlocked) Error.copy(alpha = 0.1f) else SurfaceBG)
        .clickable(
            onClick = { if(!isBefore) onSlotClick() },
            interactionSource = interactionSource,
            indication = if(isBefore) null else ripple()
        ),
        contentAlignment = Alignment.Center
    ) {
        when {
            isBlocked -> {
                Text(
                    text = stringResource(R.string.blocked),
                    color = Error,
                )
            }
            isBefore -> {
                Text(
                    color = Divider,
                    text = "Slot Vacant"
                )
            }
            else -> {
                Icon(
                    modifier = Modifier.size(45.dp),
                    painter = painterResource(R.drawable.ic_circle_plus_outline),
                    contentDescription = "Add Icon",
                    tint = Primary.copy(alpha = 0.5f)
                )
            }
        }

        if(isBlocking && !isBefore) {
            SlotCheckbox(
                isBlocked = isBlocked,
                isEnabled = true,
                onCheckedChange = { onSlotClick() },
            )
        }
    }
}
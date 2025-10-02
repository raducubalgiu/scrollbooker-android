package com.example.scrollbooker.ui.myBusiness.myCalendar.components.Slot

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.SurfaceBG

@Composable
fun CalendarFreeSlot(
    onClick: () -> Unit,
    isBlocking: Boolean,
    isBlocked: Boolean,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(if(isBlocked) Error.copy(alpha = 0.1f) else SurfaceBG)
        .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if(isBlocked) {
            Text(
                text = stringResource(R.string.blocked),
                color = Color.Red,
            )
        } else {
            Icon(
                modifier = Modifier.size(45.dp),
                painter = painterResource(R.drawable.ic_circle_plus_outline),
                contentDescription = "Add Icon",
                tint = Primary.copy(alpha = 0.2f)
            )
        }

        if(isBlocking) {
            SlotCheckbox(
                isBlocked = isBlocked,
                isEnabled = true,
                onCheckedChange = { onClick() },
            )
        }
    }
}
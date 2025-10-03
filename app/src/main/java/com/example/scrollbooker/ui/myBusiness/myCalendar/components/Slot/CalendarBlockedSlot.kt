package com.example.scrollbooker.ui.myBusiness.myCalendar.components.Slot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.Error

@Composable
fun CalendarBlockedSlot(message: String?) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Error.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message ?: stringResource(R.string.blocked),
            color = Error
        )
    }

    SlotCheckbox(
        isBlocked = true,
        isEnabled = false,
        onCheckedChange = {},
    )
}
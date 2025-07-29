package com.example.scrollbooker.ui.profile.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.ui.sharedModules.calendar.Calendar
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun CalendarScreen(
    userId: Int,
    slotDuration: Int,
    productId: Int,
    productName: String,
    onBack: () -> Unit,
    onNavigateToConfirmation: () -> Unit
) {
    Column (
        modifier = Modifier
            .background(Background)
            .safeDrawingPadding()
    ) {
        Header(
            onBack = onBack,
            customTitle = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = titleMedium,
                        color = OnBackground,
                        fontWeight = FontWeight.Bold,
                        text = stringResource(R.string.calendar)
                    )
                    Text(
                        style = titleMedium,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                        text = productName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )

        Calendar(
            userId = userId,
            productId = productId,
            slotDuration = slotDuration,
            onSelectSlot = { onNavigateToConfirmation() }
        )
    }
}
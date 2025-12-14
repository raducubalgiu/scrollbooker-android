package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.iconButton.CustomIconButton
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeader
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderState
import com.example.scrollbooker.ui.myBusiness.myCalendar.components.header.MyCalendarHeaderStateAction
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun MyCalendarHeaderSection(
    state: MyCalendarHeaderState,
    period: String,
    onBack: () -> Unit,
    onAction: (MyCalendarHeaderStateAction) -> Unit
) {
    Header(
        customTitle = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.calendar),
                    color = Color.Gray,
                    style = bodyLarge
                )
                Text(
                    text = period,
                    fontWeight = FontWeight.SemiBold,
                    style = titleMedium
                )
            }
        },
        onBack = onBack,
        actions = {
            CustomIconButton(
                painter = R.drawable.ic_settings_outline,
                onClick = {}
            )
        }
    )

    MyCalendarHeader(
        state = state,
        onAction = onAction,
    )
}
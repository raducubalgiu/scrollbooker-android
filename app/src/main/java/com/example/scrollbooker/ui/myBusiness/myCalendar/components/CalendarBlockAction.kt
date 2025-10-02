package com.example.scrollbooker.ui.myBusiness.myCalendar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun CalendarBlockAction(
    isEnabled: Boolean,
    onBlockConfirm: () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = BasePadding)
        .background(Background)
    ) {
        HorizontalDivider(color = Divider, thickness = 0.55.dp)
        MainButton(
            modifier = Modifier.padding(BasePadding),
            onClick = {},
            enabled = isEnabled,
            title = stringResource(R.string.blocked)
        )
    }
}
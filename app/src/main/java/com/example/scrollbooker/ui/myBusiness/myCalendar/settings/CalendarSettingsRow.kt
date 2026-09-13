package com.example.scrollbooker.ui.myBusiness.myCalendar.settings

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.components.core.list.ItemListInfo

@Composable
fun CalendarSettingsRow(
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
    value: String,
    onClick: () -> Unit
) {
    ItemListInfo(
        headLine = stringResource(titleRes),
        description = stringResource(descriptionRes),
        supportingText = value,
        onClick = onClick
    )
}

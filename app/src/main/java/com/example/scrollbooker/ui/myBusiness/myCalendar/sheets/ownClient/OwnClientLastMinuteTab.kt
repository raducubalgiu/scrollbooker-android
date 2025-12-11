package com.example.scrollbooker.ui.myBusiness.myCalendar.sheets.ownClient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.scrollbooker.core.util.Dimens.BasePadding

@Composable
fun OwnClientLastMinuteTab(
    buttonHeightDp: Dp
) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = buttonHeightDp + BasePadding)
    ) {
        Text("Last Minute")
    }
}
package com.example.scrollbooker.screens.profile.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.layout.Layout
import com.example.scrollbooker.modules.calendar.CalendarViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingXXL

@Composable
fun AppointmentConfirmationScreen(
    viewModel: CalendarViewModel,
    onBack: () -> Unit
) {
    val selectedSlot by viewModel.selectedSlot.collectAsState()

    Layout(
        modifier = Modifier.safeDrawingPadding(),
        headerTitle = "Confirma programarea",
        onBack = onBack,
        enablePaddingH = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Spacer(Modifier.height(SpacingXXL))
                Text(
                    text = selectedSlot?.startDateUtc ?: ""
                )
            }
            MainButton(
                modifier = Modifier.padding(horizontal = BasePadding),
                title = stringResource(R.string.book),
                onClick = {}
            )
        }
    }
}
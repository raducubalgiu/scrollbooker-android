package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.ui.theme.Error

@Composable
fun AppointmentDetailsActions(
    modifier: Modifier = Modifier,
    status: AppointmentStatusEnum?,
    isCustomer: Boolean,
    onNavigateToCancel: () -> Unit,
    onShowBookingsSheet: () -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        when(status) {
            AppointmentStatusEnum.IN_PROGRESS -> {
                MainButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Error,
                        contentColor = Color.White,
                    ),
                    title = stringResource(R.string.cancel),
                    onClick = onNavigateToCancel
                )
            }
            AppointmentStatusEnum.FINISHED -> {
                if(isCustomer) {
                    MainButton(
                        title = stringResource(R.string.bookAgain),
                        onClick = onShowBookingsSheet
                    )
                }
            }
            else -> Unit
        }
    }
}
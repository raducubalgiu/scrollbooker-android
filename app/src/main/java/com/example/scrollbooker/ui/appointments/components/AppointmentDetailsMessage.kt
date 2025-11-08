package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun AppointmentDetailsMessage(message: String) {
    Row(
        modifier = Modifier.padding(top = SpacingXL),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = null,
            tint = Error
        )
        Spacer(Modifier.width(SpacingS))
        Text(
            text = "${stringResource(R.string.cancelReason)}: $message",
            color = Error,
            style = bodyMedium
        )
    }
}
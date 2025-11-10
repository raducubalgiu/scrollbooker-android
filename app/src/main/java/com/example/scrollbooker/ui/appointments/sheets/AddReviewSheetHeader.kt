package com.example.scrollbooker.ui.appointments.sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentUser
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AddReviewSheetHeader(
    user: AppointmentUser,
    onClose: () -> Unit
) {
    SheetHeader(
        customTitle = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Avatar(url = user.avatar ?: "", size = 25.dp)

                Spacer(Modifier.width(SpacingS))

                Text(
                    text = user.fullName,
                    fontWeight = FontWeight.SemiBold,
                    style = titleMedium
                )
            }
        },
        onClose = onClose
    )
}
package com.example.scrollbooker.ui.appointments.components.AppointmentCard
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.getDay
import com.example.scrollbooker.entity.booking.appointment.domain.model.getMonth
import com.example.scrollbooker.entity.booking.appointment.domain.model.getStatusColor
import com.example.scrollbooker.entity.booking.appointment.domain.model.getStatusRes
import com.example.scrollbooker.entity.booking.appointment.domain.model.getTime
import com.example.scrollbooker.ui.theme.bodySmall
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AppointmentCard(
    appointment: Appointment,
    navigateToAppointmentDetails: (Appointment) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = { navigateToAppointmentDetails(appointment) }
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(appointment.getStatusRes()),
                color = appointment.getStatusColor(),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingXL))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    AppointmentCardInfo(appointment)
                }

                Spacer(Modifier.width(SpacingS))

                AppointmentCardDate(
                    day = appointment.getDay(),
                    month = appointment.getMonth(),
                    startTime = appointment.getTime()
                )
            }
        }

        Spacer(Modifier.height(BasePadding))

        if(appointment.status == AppointmentStatusEnum.FINISHED) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Videocam,
                    tint = Color.Gray.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp),
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.youCanAddVideoReview),
                    style = bodySmall,
                    color = Color.Gray.copy(alpha = 0.85f)
                )
            }
        }
    }
}
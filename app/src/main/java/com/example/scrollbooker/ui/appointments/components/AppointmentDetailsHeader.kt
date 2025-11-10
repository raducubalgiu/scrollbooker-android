package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.displayAppointmentDate
import com.example.scrollbooker.entity.booking.appointment.domain.model.getStatusColor
import com.example.scrollbooker.entity.booking.appointment.domain.model.getStatusRes
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.headlineMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AppointmentDetailsHeader(
    appointment: Appointment
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier
            .clip(ShapeDefaults.Medium)
            .background(appointment.getStatusColor().copy(0.2f))
            .padding(vertical = SpacingXS, horizontal = SpacingM)
        ) {
            Text(
                text = stringResource(appointment.getStatusRes()),
                color = appointment.getStatusColor(),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(SpacingXL))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = appointment.displayAppointmentDate(),
                style = headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingS))

            Text(
                text = "(${appointment.totalDuration} min)",
                style = bodyLarge,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(SpacingXL))

        Row(verticalAlignment = Alignment.CenterVertically) {
            if(appointment.user.ratingsAverage != null) {
                AvatarWithRating(
                    url = appointment.user.avatar ?: "",
                    onClick = {},
                    rating = appointment.user.ratingsAverage,
                )
            } else {
                Avatar(
                    url = appointment.user.avatar ?: "",
                    onClick = {},
                )
            }

            Spacer(Modifier.width(SpacingM))

            Column {
                Text(
                    text = appointment.user.fullName,
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${appointment.user.profession} • ${appointment.user.ratingsCount} ${stringResource(R.string.reviews)}",
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
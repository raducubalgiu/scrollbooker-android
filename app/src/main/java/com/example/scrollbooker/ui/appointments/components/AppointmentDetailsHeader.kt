package com.example.scrollbooker.ui.appointments.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarGroup
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.extensions.display
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.displayedPerson
import com.example.scrollbooker.entity.booking.appointment.domain.model.getStatusColor
import com.example.scrollbooker.entity.booking.appointment.domain.model.getStatusRes
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleLarge
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
                style = bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(Modifier.height(SpacingXL))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = appointment.startDate.display(),
                style = titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingS))

            Text(
                text = "(${appointment.totalDuration} min)",
                style = bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(SpacingXL))

        val displayedPerson = appointment.displayedPerson()

        Row(verticalAlignment = Alignment.CenterVertically) {
            if(displayedPerson.ratingsAverage != null) {
                AvatarWithRating(
                    url = displayedPerson.avatar ?: "",
                    onClick = {},
                    rating = displayedPerson.ratingsAverage,
                )
            } else {
                Avatar(
                    url = displayedPerson.avatar ?: "",
                    onClick = {},
                )
            }

            Spacer(Modifier.width(SpacingM))

            Column {
                Text(
                    text = displayedPerson.fullName,
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!displayedPerson.profession.isNullOrBlank()) {
                    Text(
                        text = if (displayedPerson.ratingsCount != null) {
                            "${displayedPerson.profession} • ${displayedPerson.ratingsCount} ${stringResource(R.string.reviews)}"
                        } else {
                            displayedPerson.profession
                        },
                        style = bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        if (!appointment.isCustomer) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = SpacingXL)
            ) {
                Text(
                    text = stringResource(R.string.specialist),
                    style = bodyMedium,
                    color = Color.Gray,
                    fontSize = 12.sp
                )

                Spacer(Modifier.width(SpacingS))

                Icon(
                    painter = painterResource(R.drawable.ic_reload),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(12.dp)
                )

                Spacer(Modifier.width(SpacingS))

                AvatarGroup(
                    avatarUrls = listOf(
                        appointment.business.businessOwnerAvatar.orEmpty(),
                        appointment.user.avatar.orEmpty()
                    )
                )

                Spacer(Modifier.width(SpacingS))

                Text(
                    text = appointment.user.fullName,
                    style = bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
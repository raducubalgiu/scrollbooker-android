package com.example.scrollbooker.ui.appointments.components.AppointmentCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.components.core.avatar.AvatarWithRating
import com.example.scrollbooker.core.extensions.formatDuration
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.getProductNames
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import com.mapbox.maps.extension.style.expressions.dsl.generated.image
import java.math.BigDecimal

@Composable
fun AppointmentCardInfo(appointment: Appointment) {
    val user = appointment.user
    val customer = appointment.customer
    val ratingsAverage = appointment.user.ratingsAverage

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if(appointment.isCustomer && ratingsAverage != null) {
                AvatarWithRating(
                    url = user.avatar ?: "",
                    onClick = {},
                    rating = ratingsAverage,
                    size = 60.dp,
                )
            } else {
                Avatar(
                    url = customer.avatar ?: "",
                    size = 60.dp,
                )
            }
            Spacer(Modifier.width(SpacingM))

            Column {
                Text(
                    text = if(appointment.isCustomer) user.fullName else customer.fullName,
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = (if(appointment.isCustomer) user.profession else customer.profession).toString(),
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(Modifier.height(BasePadding))

        Text(
            text = appointment.getProductNames(),
            style = titleMedium,
            fontSize = 17.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(SpacingS))

        Row {
            Icon(
                painter = painterResource(R.drawable.ic_clock_outline),
                contentDescription = null,
                tint = Color.Gray
            )
            Spacer(Modifier.width(SpacingS))

            Text(
                color = Color.Gray,
                text = appointment.totalDuration.formatDuration(),
            )
        }

        Spacer(Modifier.height(SpacingS))

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${appointment.totalPriceWithDiscount.toTwoDecimals()} ${appointment.paymentCurrency.name}",
                    style = titleMedium,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(SpacingS))

                if(appointment.totalDiscount > BigDecimal.ZERO) {
                    Text(
                        text = appointment.totalPrice.toTwoDecimals(),
                        style = bodyMedium,
                        textDecoration = TextDecoration.LineThrough,
                        color = Color.Gray
                    )
                    Spacer(Modifier.width(SpacingS))
                    Text(
                        text = "(-${appointment.totalDiscount.toTwoDecimals()}%)",
                        color = Error
                    )
                }
            }
        }
    }
}
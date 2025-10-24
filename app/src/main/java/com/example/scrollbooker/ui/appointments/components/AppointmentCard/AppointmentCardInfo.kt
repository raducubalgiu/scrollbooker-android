package com.example.scrollbooker.ui.appointments.components.AppointmentCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scrollbooker.components.core.avatar.Avatar
import com.example.scrollbooker.core.extensions.toTwoDecimals
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.getProductNames
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AppointmentCardInfo(appointment: Appointment) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(
                url = (if(appointment.isCustomer) appointment.user.avatar
                        else appointment.customer.avatar).toString(),
                size = 50.dp
            )
            Spacer(Modifier.width(SpacingM))

            Column {
                Text(
                    text = if(appointment.isCustomer) appointment.user.fullName
                        else appointment.customer.fullName,
                    style = titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = (if(appointment.isCustomer) appointment.user.profession
                    else appointment.customer.profession).toString(),
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(Modifier.height(SpacingM))

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

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${appointment.totalPrice.toTwoDecimals()} ${appointment.paymentCurrency.name}",
                    style = titleMedium,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(SpacingS))

//                if(discount > BigDecimal.ZERO) {
//                    Text(
//                        text = appointment.totalPrice,
//                        style = bodyMedium,
//                        textDecoration = TextDecoration.LineThrough
//                    )
//                    Spacer(Modifier.width(SpacingS))
//                    Text(
//                        text = "(-${discount}%)",
//                        color = Error
//                    )
//                }
            }
        }
    }
}
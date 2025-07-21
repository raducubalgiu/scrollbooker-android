package com.example.scrollbooker.screens.appointments.components.AppointmentCard
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.ui.theme.titleMedium
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

@Composable
fun AppointmentCard(
    appointment: Appointment,
    navigateToAppointmentDetails: (Appointment) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            navigateToAppointmentDetails(appointment)
        }
    ) {
        val zone = ZoneId.systemDefault()
        val appointmentStart = appointment.startDate.withZoneSameInstant(zone)

        val day = appointmentStart.format(DateTimeFormatter.ofPattern("dd"))
        val month = appointmentStart.format(DateTimeFormatter.ofPattern("MMMM", Locale("ro")))
        val startTime = appointmentStart.format(DateTimeFormatter.ofPattern("HH:mm"))

        val supportingText = if(!appointment.isCustomer)
            "@${appointment.user.username}" else appointment.user.profession

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(start = BasePadding),
                text = AppointmentStatusEnum.getLabel(appointment.status)?.let { stringResource(it) } ?: "",
                color = AppointmentStatusEnum.getColor(appointment.status) ?: Color.Gray,
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingXL))

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = BasePadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                with(appointment) {
                    Column(Modifier.weight(1f)) {
                        AppointmentCardInfo(
                            avatar = user.avatar ?: "",
                            fullName = user.fullName,
                            usernameOrProfession = supportingText ?: "",
                            productName = product.name,
                            price = product.price,
                            priceWithDiscount = product.priceWithDiscount,
                            discount = product.discount,
                            currency = product.currency
                        )
                    }
                    AppointmentCardDate(day, month, startTime)
                }
            }

            if(AppointmentStatusEnum.fromKey(appointment.status) == AppointmentStatusEnum.FINISHED) {
                Spacer(Modifier.padding(vertical = SpacingM))

                MainButtonOutlined(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BasePadding),
                    shape = ShapeDefaults.Small,
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 15.dp
                    ),
                    title = stringResource(R.string.bookAgain),
                    onClick = {}
                )
            }
        }
    }
}
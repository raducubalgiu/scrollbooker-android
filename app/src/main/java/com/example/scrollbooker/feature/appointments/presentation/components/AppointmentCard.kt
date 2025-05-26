package com.example.scrollbooker.feature.appointments.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Avatar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.feature.appointments.domain.model.Appointment
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnBackground
import com.example.scrollbooker.ui.theme.OnPrimary
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.Primary
import com.example.scrollbooker.ui.theme.ScrollBookerTheme
import com.example.scrollbooker.ui.theme.bodyLarge
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.labelLarge
import com.example.scrollbooker.ui.theme.titleSmall

@Composable
fun AppointmentCard(appointment: Appointment) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {})
    ) {
        val supportingText = if(appointment.isCustomer) "@${appointment.user.username}" else appointment.user.profession
        val channelMessage = if(appointment.channel == "scroll_booker") "Scroll Booker" else "Client propriu"
        val chipColor = if(appointment.channel == "scroll_booker") Primary else Error

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Avatar(url = appointment.user.avatar)
                    Spacer(modifier = Modifier.width(BasePadding))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                style = titleSmall,
                                text = appointment.user.fullName,
                                fontWeight = FontWeight.ExtraBold,
                                color = OnBackground
                            )
                            if(!appointment.isCustomer) {
                                Spacer(Modifier.width(BasePadding))
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_star),
                                    contentDescription = null,
                                    tint = Primary
                                )
                                Spacer(Modifier.width(SpacingXS))
                                Text(
                                    style = bodyLarge,
                                    text = appointment.user.ratingsAverage.toString(),
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OnBackground
                                )
                            }
                        }
                        Spacer(Modifier.height(SpacingXXS))
                        Text(
                            style = labelLarge,
                            text = supportingText,
                            fontWeight = FontWeight.Normal,
                            color = OnSurfaceBG
                        )
                    }
                }

                if(appointment.isCustomer) {
                    SuggestionChip(
                        label = { Text(channelMessage) },
                        onClick = {},
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = chipColor,
                            labelColor = OnPrimary
                        ),
                        shape = ShapeDefaults.ExtraSmall,
                        border = BorderStroke(width = 0.dp, color = Color.Transparent)
                    )
                }
            }

            Spacer(Modifier.height(BasePadding))

            Text(
                color = OnBackground,
                style = bodyMedium,
                fontWeight = FontWeight.Bold,
                text = "Joi, 15 Mai - 17:30"
            )

            Spacer(Modifier.height(SpacingM))

            Text(
                style = bodyMedium,
                fontWeight = FontWeight.Bold,
                color = OnBackground,
                text = appointment.product.name
            )

            Spacer(Modifier.height(SpacingM))

            Text(
                style = bodyMedium,
                fontWeight = FontWeight.Bold,
                color = OnBackground,
                text = "${appointment.product.price} ${appointment.product.currency}"
            )

            Spacer(Modifier.height(BasePadding))

            if(appointment.status == "finished" && !appointment.isCustomer) {
                Button(
                    onClick = {  },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(text = stringResource(id = R.string.bookAgain)) }
            }
        }
    }

    HorizontalDivider()
}

@Composable
@Preview(name = "Light", showBackground = true)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun AppointmentCardPreview() {
    ScrollBookerTheme() {
        AppointmentCard(
            appointment = TODO()
        )
    }
}
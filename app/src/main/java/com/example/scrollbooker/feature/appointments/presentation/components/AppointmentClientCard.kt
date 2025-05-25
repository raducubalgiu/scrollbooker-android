package com.example.scrollbooker.feature.appointments.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.scrollbooker.R
import com.example.scrollbooker.components.Avatar
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXS
import com.example.scrollbooker.core.util.Dimens.SpacingXXS
import com.example.scrollbooker.feature.appointments.domain.model.Appointment
import com.example.scrollbooker.ui.theme.ScrollBookerTheme

@Composable
fun AppointmentClientCard(appointment: Appointment) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = {})
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(BasePadding),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(url = appointment.user.avatar)

                Spacer(modifier = Modifier.width(BasePadding))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            style = MaterialTheme.typography.titleSmall,
                            text = appointment.user.fullName,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.width(BasePadding))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(SpacingXS))
                        Text(
                            style = MaterialTheme.typography.bodyLarge,
                            text = appointment.user.ratingsAverage.toString(),
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(Modifier.height(SpacingXXS))
                    Text(
                        style = MaterialTheme.typography.labelLarge,
                        text = appointment.user.profession,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(BasePadding))

            Text(
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                text = "Joi, 15 Mai - 17:30"
            )

            Spacer(Modifier.height(SpacingM))

            Text(
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                text = appointment.product.name
            )

            Spacer(Modifier.height(SpacingM))

            Text(
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                text = "${appointment.product.price} ${appointment.product.currency}"
            )

            Spacer(Modifier.height(BasePadding))

            if(appointment.status == "finished") {
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
        AppointmentClientCard(
            appointment = TODO()
        )
    }
}
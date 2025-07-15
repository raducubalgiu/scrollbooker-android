package com.example.scrollbooker.screens.appointments
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.screens.appointments.components.AppointmentCard.AppointmentCard
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun AppointmentDetailsScreen(
    viewModel: AppointmentsViewModel,
    onBack: () -> Unit,
    onGoToCancel: () -> Unit
) {
    val appointment by viewModel.selectedAppointment.collectAsState()
    val status = AppointmentStatusEnum.fromKey(appointment?.status)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Header(
            title = "",
            onBack = onBack
        )

        Spacer(Modifier.height(SpacingXL))

        Column(modifier = Modifier
            .weight(1f)
            .padding(horizontal = BasePadding)
        ) {
            if(appointment != null) {
                AppointmentCard(
                    appointment = appointment!!,
                    navigateToAppointmentDetails = {}
                )
            }

            Spacer(Modifier.height(SpacingXL))

            when(status) {
                AppointmentStatusEnum.IN_PROGRESS -> {
                    MainButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Error,
                            contentColor = Color.White,
                        ),
                        title = stringResource(R.string.cancel),
                        onClick = onGoToCancel
                    )
                }
                AppointmentStatusEnum.FINISHED -> {
                    MainButton(
                        title = stringResource(R.string.bookAgain),
                        onClick = {}
                    )
                }
                else -> Unit
            }

            Spacer(Modifier.height(SpacingXL))

            Text(
                text = stringResource(R.string.location),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(SpacingXL))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_outline),
                    contentDescription = null,
                    tint = Color.Gray
                )
                Spacer(Modifier.width(SpacingM))
                Text(text = appointment?.business?.address ?: "")
            }

            Spacer(Modifier.height(BasePadding))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = ShapeDefaults.Medium)
                    .height(200.dp)
                    .background(SurfaceBG)
            ) {

            }

            Spacer(Modifier.height(BasePadding))
        }
    }
}
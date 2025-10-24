package com.example.scrollbooker.ui.appointments
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.sheet.Sheet
import com.example.scrollbooker.core.enums.AppointmentStatusEnum
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.navigation.navigators.NavigateCalendarParam
import com.example.scrollbooker.ui.appointments.components.AppointmentCard.AppointmentCard
import com.example.scrollbooker.ui.shared.location.LocationSection
import com.example.scrollbooker.ui.shared.posts.sheets.bookings.BookingsSheet
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDetailsScreen(
    viewModel: AppointmentsViewModel,
    onBack: () -> Unit,
    onNavigateToCancel: () -> Unit,
    onNavigateToBookAgain: (NavigateCalendarParam) -> Unit
) {
    val appointment by viewModel.selectedAppointment.collectAsState()

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if(sheetState.isVisible) {
        Sheet(
            modifier = Modifier.statusBarsPadding(),
            sheetState = sheetState,
            onClose = { scope.launch { sheetState.hide() } }
        ) {
            appointment?.user?.id?.let { userId ->
                BookingsSheet(
                    userId = 13,
                    initialPage = 0,
                    onClose = { scope.launch { sheetState.hide() } }
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Header(
            title = "",
            onBack = onBack
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SpacingXL)
        ) {
            appointment?.let { a ->
                AppointmentCard(
                    appointment = a,
                    navigateToAppointmentDetails = {}
                )

                Spacer(Modifier.height(SpacingXL))

                when(a.status) {
                    AppointmentStatusEnum.IN_PROGRESS -> {
                        MainButton(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Error,
                                contentColor = Color.White,
                            ),
                            title = stringResource(R.string.cancel),
                            onClick = onNavigateToCancel
                        )
                    }
                    AppointmentStatusEnum.FINISHED -> {
                        MainButton(
                            title = stringResource(R.string.bookAgain),
                            onClick = { scope.launch { sheetState.show() } }
                        )
                    }
                    else -> Unit
                }

                a.message?.let {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Warning,
                                contentDescription = null,
                                tint = Error
                            )
                            Spacer(Modifier.width(SpacingS))
                            Text(
                                text = "${stringResource(R.string.cancelReason)}: ${appointment?.message ?: ""}",
                                color = Error,
                                style = bodyMedium
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(SpacingXL))

            Text(
                text = stringResource(R.string.location),
                style = titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LocationSection()
        }
    }
}
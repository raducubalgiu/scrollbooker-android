package com.example.scrollbooker.ui.appointments
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.core.util.Dimens.BasePadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.ui.appointments.components.AppointmentCancelEnum
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnPrimary

@Composable
fun AppointmentCancelScreen(
    viewModel: AppointmentsViewModel,
    onBack: () -> Unit,
    onCancelAppointment: (appointmentId: Int, message: String) -> Unit
) {
    val appointment by viewModel.selectedAppointment.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val reasons = listOf(
        AppointmentCancelEnum.CANNOT_ARRIVE.key,
        AppointmentCancelEnum.FOUND_BETTER_OFFER.key,
        AppointmentCancelEnum.BOOKED_BY_MISTAKE.key,
        AppointmentCancelEnum.OTHER_REASON.key
    )

    var message by remember { mutableStateOf("") }
    var selectedReason by rememberSaveable { mutableStateOf(AppointmentCancelEnum.OTHER_REASON.key) }

    val checkMessage = checkLength(LocalContext.current, maxLength = 100, field = message)
    val isMessageValid = checkMessage.isNullOrEmpty()

    Column(modifier = Modifier.fillMaxSize()) {
        Header(
            modifier = Modifier.statusBarsPadding(),
            title = "",
            onBack = onBack
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn {
                itemsIndexed(reasons) { index, reason ->
                    InputRadio(
                        selected = reason == selectedReason,
                        onSelect = {
                            selectedReason = reason

                            if(reason in reasons) {
                                message = ""
                            }
                        },
                        headLine = stringResource(
                            AppointmentCancelEnum.label(
                                AppointmentCancelEnum.fromKey(reason)
                            )
                        )
                    )

                    if(index < reasons.size) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = BasePadding),
                            color = Divider,
                            thickness = 0.55.dp
                        )
                    }
                }

                item {
                    EditInput(
                        modifier = Modifier.padding(horizontal = BasePadding),
                        value = message,
                        onValueChange = { message = it },
                        singleLine = false,
                        placeholder = stringResource(R.string.writeCancelReason),
                        minLines = 3,
                        maxLines = 3,
                        isEnabled = selectedReason == AppointmentCancelEnum.OTHER_REASON.key,
                        isInputValid = isMessageValid,
                        errorMessage = checkMessage.toString(),
                    )
                }
            }

            MainButton(
                title = stringResource(R.string.cancelAppointment),
                enabled = message.isNotEmpty() && isMessageValid && !isSaving,
                isLoading = isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Error,
                    contentColor = OnPrimary
                ),
                modifier = Modifier
                    .padding(horizontal = BasePadding)
                    .padding(bottom = BasePadding),
                onClick = {
                    appointment?.id?.let {
                        onCancelAppointment(appointment!!.id, message)
                    }
                }
            )
        }
    }
}
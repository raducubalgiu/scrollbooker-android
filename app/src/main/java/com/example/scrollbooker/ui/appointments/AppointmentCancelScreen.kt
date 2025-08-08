package com.example.scrollbooker.ui.appointments
import androidx.compose.foundation.background
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.EditInput
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.core.util.Dimens.SpacingXL
import com.example.scrollbooker.core.util.checkLength
import com.example.scrollbooker.ui.appointments.components.AppointmentCancelEnum
import com.example.scrollbooker.ui.theme.Background
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
        "Nu mai pot ajunge la programare",
        "Am găsit o ofertă mai bună",
        "Am rezervat din greșeală",
        "Alt motiv"
    )

    var message by rememberSaveable { mutableStateOf("") }
    var selectedReason by rememberSaveable { mutableStateOf("Alt Motiv") }

    val checkMessage = checkLength(LocalContext.current, maxLength = 100, field = message)
    val isMessageValid = checkMessage.isNullOrEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .safeDrawingPadding()
    ) {
        Header(
            modifier = Modifier.statusBarsPadding(),
            title = stringResource(R.string.cancelAppointment),
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
                        headLine = reason
                    )

                    if(index < reasons.size) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = SpacingXL),
                            color = Divider,
                            thickness = 0.55.dp
                        )
                    }
                }

                item {
                    EditInput(
                        modifier = Modifier
                            .padding(horizontal = BasePadding),
                        value = message,
                        onValueChange = { message = it },
                        singleLine = false,
                        placeholder = stringResource(R.string.writeCancelReason),
                        minLines = 5,
                        maxLines = 5,
                        isEnabled = selectedReason == AppointmentCancelEnum.OTHER_REASON.key,
                        isInputValid = isMessageValid,
                        errorMessage = checkMessage.toString(),
                    )
                }
            }

            MainButton(
                title = stringResource(R.string.cancelAppointment),
                enabled = (message.isNotEmpty() || selectedReason != "Alt Motiv") && isMessageValid && !isSaving,
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
                        if(selectedReason == "Alt Motiv") {
                            onCancelAppointment(appointment!!.id, message)
                        } else {
                            onCancelAppointment(appointment!!.id, selectedReason)
                        }
                    }
                }
            )
        }
    }
}
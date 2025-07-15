package com.example.scrollbooker.screens.appointments
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
        stringResource(R.string.cannotArriveToAppointment),
        stringResource(R.string.iFoundABetterOffer),
        stringResource(R.string.iBookedByMistake),
        stringResource(R.string.otherReason)
    )

    var message by remember { mutableStateOf("") }
    var selectedReason by rememberSaveable { mutableStateOf("Alt Motiv") }

    val checkMessage = checkLength(
        LocalContext.current, maxLength = 100, field = message
    )
    val isMessageValid = checkMessage.isNullOrEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        Header(
            title = "",
            onBack = onBack
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(reasons) { index, reason ->
                    InputRadio(
                        selected = reason == selectedReason,
                        onSelect = {
                            selectedReason = reason
                            if(reason == "Altele") message = "" else message = reason
                        },
                        headLine = reason
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
                        placeholder = "Scrie motivul",
                        minLines = 3,
                        maxLines = 3,
                        isEnabled = selectedReason == "Altele",
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
                    appointment?.id?.let { onCancelAppointment(appointment!!.id, message) }
                }
            )
        }
    }
}
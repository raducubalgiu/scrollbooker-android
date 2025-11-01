package com.example.scrollbooker.ui.appointments
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnPrimary

enum class AppointmentCancelReason {
    CANNOT_ARRIVE,
    FOUND_BETTER_OFFER,
    BOOKED_BY_MISTAKE,
    OTHER
}

@StringRes
fun AppointmentCancelReason.toLabel(): Int = when(this) {
    AppointmentCancelReason.CANNOT_ARRIVE -> R.string.reasonCancel_cannotArrive
    AppointmentCancelReason.FOUND_BETTER_OFFER -> R.string.reasonCancel_foundBetterOffer
    AppointmentCancelReason.BOOKED_BY_MISTAKE -> R.string.reasonCancel_bookedByMistake
    AppointmentCancelReason.OTHER -> R.string.others
}

@Composable
fun AppointmentCancelScreen(
    viewModel: AppointmentsViewModel,
    onBack: () -> Unit,
    onCancelAppointment: (appointmentId: Int, message: String) -> Unit
) {
    val context = LocalContext.current
    //val appointment by viewModel.selectedAppointment.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()

    val reasons = AppointmentCancelReason.entries.toList()

    var message by rememberSaveable { mutableStateOf("") }
    var selectedReason by rememberSaveable { mutableStateOf(AppointmentCancelReason.OTHER) }

    val maxLength = 100
    val checkMessage = checkLength(LocalContext.current, maxLength = maxLength, field = message)
    val isMessageValid = checkMessage.isNullOrEmpty()

    val isOtherReason = selectedReason == AppointmentCancelReason.OTHER

    Scaffold(
        topBar = {
            Header(
                title = stringResource(R.string.cancelAppointment),
                onBack = onBack
            )
        },
        bottomBar = {
            HorizontalDivider(
                color = Divider,
                thickness = 0.55.dp
            )
            Column {
                HorizontalDivider(color = Divider, thickness = 0.55.dp)
                MainButton(
                    modifier = Modifier.padding(BasePadding),
                    title = stringResource(R.string.cancelAppointment),
                    enabled = (message.isNotEmpty() || !isOtherReason) && isMessageValid && !isSaving,
                    isLoading = isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Error,
                        contentColor = OnPrimary
                    ),
                    onClick = {
//                        appointment?.id?.let { id ->
//                            if(isOtherReason) onCancelAppointment(id, message)
//                            else onCancelAppointment(
//                                id, context.getString(selectedReason.toLabel())
//                            )
//                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            LazyColumn(Modifier.weight(1f)) {
                itemsIndexed(reasons) { index, reason ->
                    InputRadio(
                        selected = reason == selectedReason,
                        onSelect = {
                            selectedReason = reason

                            if(reason in reasons) {
                                message = ""
                            }
                        },
                        headLine = stringResource(reason.toLabel())
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
                    AnimatedVisibility(isOtherReason) {
                        EditInput(
                            modifier = Modifier.padding(horizontal = BasePadding),
                            value = message,
                            onValueChange = { message = it },
                            singleLine = false,
                            placeholder = stringResource(R.string.writeCancelReason),
                            minLines = 5,
                            maxLines = 5,
                            maxLength = maxLength,
                            isEnabled = selectedReason == AppointmentCancelReason.OTHER && !isSaving,
                            errorMessage = checkMessage.toString(),
                        )
                    }
                }
            }
        }
    }
}
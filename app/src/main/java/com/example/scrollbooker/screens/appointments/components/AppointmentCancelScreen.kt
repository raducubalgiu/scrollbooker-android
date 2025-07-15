package com.example.scrollbooker.screens.appointments.components

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
import com.example.scrollbooker.screens.appointments.AppointmentsViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.components.core.headers.Header
import com.example.scrollbooker.components.core.inputs.InputRadio
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnPrimary

@Composable
fun AppointmentCancelScreen(
    viewModel: AppointmentsViewModel,
    onBack: () -> Unit
) {
    val appointment by viewModel.selectedAppointment.collectAsState()

    val reasons = listOf(
        "Nu mai pot ajunge la programare",
        "Am gasit o oferta mai buna",
        "Altele"
    )

    var selectedReason by rememberSaveable { mutableStateOf("") }

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
            Column(Modifier.weight(1f)) {
                LazyColumn {
                    itemsIndexed(reasons) { index, reason ->
                        InputRadio(
                            selected = reason == selectedReason,
                            onSelect = { selectedReason = reason },
                            headLine = reason
                        )

                        if(index < reasons.size) {
                            HorizontalDivider(color = Divider, thickness = 0.55.dp)
                        }
                    }
                }
            }
            MainButton(
                modifier = Modifier.padding(horizontal = BasePadding),
                title = stringResource(R.string.cancel),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Error,
                    contentColor = OnPrimary
                ),
                onClick = {}
            )
        }
    }
}
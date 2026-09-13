package com.example.scrollbooker.ui.myBusiness.myCalendar.settings

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.scrollbooker.R
import com.example.scrollbooker.components.core.buttons.MainButton
import com.example.scrollbooker.components.core.buttons.MainButtonOutlined
import com.example.scrollbooker.components.core.dialog.DialogConfirm
import com.example.scrollbooker.components.core.sheet.SheetHeader
import com.example.scrollbooker.core.extensions.findActivity
import com.example.scrollbooker.core.snackbar.rememberSnackBarController
import com.example.scrollbooker.core.util.Dimens.BasePadding
import com.example.scrollbooker.core.util.Dimens.SpacingL
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.calendar.connection.domain.model.CalendarConnection
import com.example.scrollbooker.entity.calendar.connection.domain.model.isActive
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.SurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium
import com.example.scrollbooker.ui.theme.titleMedium

@Composable
fun rememberGoogleCalendarStatusLabel(): String {
    val connectionState by hiltViewModel<CalendarConnectionViewModel>()
        .connectionState.collectAsStateWithLifecycle()

    return when (val state = connectionState) {
        is FeatureState.Loading -> ""
        is FeatureState.Error -> stringResource(R.string.calendarNotConnected)
        is FeatureState.Success ->
            if (state.data?.isActive() == true) stringResource(R.string.calendarConnected)
            else stringResource(R.string.calendarNotConnected)
    }
}

@Composable
fun GoogleCalendarSheet(onClose: () -> Unit) {
    val viewModel: CalendarConnectionViewModel = hiltViewModel()
    val context = LocalContext.current

    val connectionState by viewModel.connectionState.collectAsStateWithLifecycle()
    val isProcessing by viewModel.isProcessing.collectAsStateWithLifecycle()
    val resolutionRequest by viewModel.resolutionRequest.collectAsStateWithLifecycle()

    val hostState = remember { SnackbarHostState() }
    val snackBarController = rememberSnackBarController(hostState)

    var showDisconnectConfirm by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event -> snackBarController.show(event) }
    }

    val resolutionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val activity = context.findActivity()
        if (activity != null) {
            if (result.resultCode == RESULT_OK) {
                viewModel.onResolutionResult(activity, result.data)
            } else {
                viewModel.onResolutionCancelled()
            }
        }
    }

    LaunchedEffect(resolutionRequest) {
        val intentSender = resolutionRequest ?: return@LaunchedEffect
        resolutionLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
        viewModel.onResolutionHandled()
    }

    if (showDisconnectConfirm) {
        DialogConfirm(
            title = stringResource(R.string.disconnectGoogleCalendar),
            text = stringResource(R.string.disconnectGoogleCalendarConfirm),
            confirmText = stringResource(R.string.disconnectGoogleCalendar),
            isLoading = isProcessing,
            onDismissRequest = { showDisconnectConfirm = false },
            onConfirmation = {
                showDisconnectConfirm = false
                viewModel.disconnect()
            }
        )
    }

    Column(modifier = Modifier.padding(bottom = SpacingL)) {
        SheetHeader(
            title = stringResource(R.string.calendarConnection),
            onClose = onClose
        )

        Column(modifier = Modifier.padding(horizontal = BasePadding)) {
            Text(
                text = stringResource(R.string.calendarConnectionDescription),
                style = bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(SpacingL))

            when (val state = connectionState) {
                is FeatureState.Loading -> CircularProgressIndicator(modifier = Modifier.size(24.dp))

                is FeatureState.Error -> Text(
                    text = stringResource(R.string.calendarConnectionLoadFailed),
                    style = bodyMedium,
                    color = Error
                )

                is FeatureState.Success -> ConnectionStatusRow(connection = state.data)
            }

            Spacer(Modifier.height(SpacingL))

            val connection = (connectionState as? FeatureState.Success)?.data

            if (connection != null && connection.isActive()) {
                MainButtonOutlined(
                    fullWidth = true,
                    title = stringResource(R.string.disconnectGoogleCalendar),
                    isEnabled = !isProcessing,
                    onClick = { showDisconnectConfirm = true }
                )
            } else {
                MainButton(
                    title = stringResource(R.string.connectGoogleCalendar),
                    isLoading = isProcessing,
                    enabled = !isProcessing,
                    onClick = {
                        context.findActivity()?.let { activity ->
                            viewModel.connectGoogleCalendar(activity)
                        }
                    }
                )
            }
        }

        SnackbarHost(hostState = hostState) { data ->
            Snackbar(
                modifier = Modifier.padding(horizontal = BasePadding, vertical = SpacingS),
                snackbarData = data,
                containerColor = Error,
                contentColor = OnError
            )
        }
    }
}

@Composable
private fun ConnectionStatusRow(connection: CalendarConnection?) {
    val isConnected = connection?.isActive() == true

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceBG, RoundedCornerShape(12.dp))
            .padding(BasePadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpacingM)
    ) {
        Icon(
            imageVector = if (isConnected) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            tint = if (isConnected) Color.Green else Color.Gray
        )

        Column {
            Text(
                text = if (isConnected) stringResource(R.string.calendarConnected)
                       else stringResource(R.string.calendarNotConnected),
                style = titleMedium,
                color = OnSurfaceBG
            )

            if (isConnected && connection.googleAccountEmail != null) {
                Text(
                    text = connection.googleAccountEmail,
                    style = bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

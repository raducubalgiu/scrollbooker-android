package com.example.scrollbooker.core.snackbar

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class CustomSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    val type: SnackBarType = SnackBarType.DEFAULT
) : SnackbarVisuals

class SnackBarController(
    private val context: Context,
    private val hostState: SnackbarHostState
) {
    private val mutex = Mutex()

    suspend fun show(event: SnackBarUiEvent.Show) = mutex.withLock {
        hostState.showSnackbar(
            CustomSnackbarVisuals(
                message = event.message.asString(context),
                actionLabel = event.actionLabel?.asString(context),
                withDismissAction = event.withDismissAction,
                duration = event.duration,
                type = event.type
            )
        )
    }
}
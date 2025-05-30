package com.example.scrollbooker.core.snackbar

import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SnackbarManager {
    val snackbarHostState = SnackbarHostState()

    fun showMessage(scope: CoroutineScope, message: SnackbarMessage) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message.message,
                actionLabel = message.actionLabel,
                duration = message.duration
            )
        }
    }

    fun showMessage(scope: CoroutineScope, message: String) {
        showMessage(scope, SnackbarMessage(message))
    }
}
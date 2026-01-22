package com.example.scrollbooker.core.snackbar

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SnackBarController(
    private val context: Context,
    private val hostState: SnackbarHostState
) {
    var currentType by mutableStateOf(SnackBarType.DEFAULT)
        private set

    private val mutex = Mutex()

    suspend fun show(event: SnackBarUiEvent.Show) = mutex.withLock {
        currentType = event.type

        hostState.showSnackbar(
            message = event.message.asString(context),
            actionLabel = event.actionLabel?.asString(context),
            withDismissAction = event.withDismissAction,
            duration = event.duration
        )
    }
}
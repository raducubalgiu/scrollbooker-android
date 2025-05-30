package com.example.scrollbooker.core.snackbar
import androidx.compose.runtime.Composable
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

object SnackbarManager {
    private val _messages = MutableSharedFlow<SnackbarMessage>()
    val messages = _messages.asSharedFlow()

    fun showMessage(message: String) {
        Timber.d("Snackbar message: $message")
        emit(SnackbarMessage(message))
    }

    fun showError(message: String) {
        Timber.e("Snackbar error: $message")
        emit(SnackbarMessage(message))
    }
    
    fun showError(error: Throwable) {
        val message = error.message?.takeIf { it.isNotBlank() } ?: "Something went wrong"

        Timber.e(error, "Snackbar error: $message")
        showMessage(message)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun emit(message: SnackbarMessage) {
        GlobalScope.launch {
            _messages.emit(message)
        }
    }
}
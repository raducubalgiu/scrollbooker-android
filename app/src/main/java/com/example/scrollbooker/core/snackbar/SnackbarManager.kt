package com.example.scrollbooker.core.snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object SnackbarManager {
    private val _messages = MutableSharedFlow<SnackbarMessage>()
    val messages = _messages.asSharedFlow()

    fun showToast(message: String) {
        emit(SnackbarMessage(message))
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun emit(message: SnackbarMessage) {
        GlobalScope.launch {
            _messages.emit(message)
        }
    }
}
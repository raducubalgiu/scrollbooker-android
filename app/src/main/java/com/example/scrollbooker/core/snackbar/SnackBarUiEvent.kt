package com.example.scrollbooker.core.snackbar

import androidx.compose.material3.SnackbarDuration
import com.example.scrollbooker.R

sealed interface SnackBarUiEvent {

    data class Show(
        val message: UiText,
        val type: SnackBarType = SnackBarType.DEFAULT,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        val withDismissAction: Boolean = false,
        val actionLabel: UiText? = null
    ) : SnackBarUiEvent

    companion object {
        fun somethingWentWrong(
            type: SnackBarType = SnackBarType.ERROR
        ) = Show(
            message = UiText.Resource(R.string.somethingWentWrong),
            type = type
        )
    }
}
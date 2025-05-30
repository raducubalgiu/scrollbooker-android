package com.example.scrollbooker.core.snackbar

import androidx.annotation.StringRes

sealed class UiEvent {
    data class ShowSnackbar(@StringRes val messageRes: Int): UiEvent()
}
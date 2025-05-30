package com.example.scrollbooker.core.snackbar

import androidx.compose.material3.SnackbarDuration

data class SnackbarMessage(
    val message: String,
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short
)
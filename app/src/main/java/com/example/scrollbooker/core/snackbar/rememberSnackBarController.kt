package com.example.scrollbooker.core.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberSnackBarController(
    hostState: SnackbarHostState
): SnackBarController {
    val context = LocalContext.current
    return remember(context, hostState) {
        SnackBarController(context, hostState)
    }
}
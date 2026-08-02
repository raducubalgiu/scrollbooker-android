package com.example.scrollbooker.core.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.scrollbooker.ui.theme.Error
import com.example.scrollbooker.ui.theme.OnError

enum class SnackBarType { DEFAULT, ERROR }

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
    type: SnackBarType = SnackBarType.DEFAULT
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter

    ) {
        SnackbarHost(
            hostState = hostState
        ) { data ->
            Snackbar(
                modifier = Modifier.statusBarsPadding(),
                snackbarData = data,
                shape = ShapeDefaults.Medium,
                containerColor = when(type) {
                    SnackBarType.DEFAULT -> MaterialTheme.colorScheme.inverseSurface
                    SnackBarType.ERROR -> Error
                },
                contentColor = when(type) {
                    SnackBarType.DEFAULT -> MaterialTheme.colorScheme.inverseOnSurface
                    SnackBarType.ERROR -> OnError
                }
            )
        }
    }
}
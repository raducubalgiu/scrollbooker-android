package com.example.scrollbooker.core.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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

enum class SnackBarAlignment { TOP, BOTTOM }
enum class SnackBarType { DEFAULT, ERROR }

@Composable
fun CustomSnackBar(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState,
    alignment: SnackBarAlignment = SnackBarAlignment.TOP,
    type: SnackBarType = SnackBarType.DEFAULT
) {
    val statusBarTop = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

    Box(
        modifier = modifier.fillMaxSize().padding(top = statusBarTop),
        contentAlignment = if(alignment == SnackBarAlignment.BOTTOM) Alignment.BottomCenter
                           else Alignment.TopCenter

    ) {
        SnackbarHost(
            hostState = hostState
        ) { data ->
            Snackbar(
                modifier = Modifier.padding(top = statusBarTop),
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
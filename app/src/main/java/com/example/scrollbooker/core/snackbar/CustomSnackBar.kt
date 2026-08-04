package com.example.scrollbooker.core.snackbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
enum class SnackBarPosition { TOP, BOTTOM }

@Composable
fun CustomSnackBar(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    position: SnackBarPosition = SnackBarPosition.TOP
) {
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = hostState,
            modifier = modifier
                .align(
                    when (position) {
                        SnackBarPosition.TOP -> Alignment.TopCenter
                        SnackBarPosition.BOTTOM -> Alignment.BottomCenter
                    }
                )
                .fillMaxWidth()
                .then(
                    when (position) {
                        SnackBarPosition.TOP -> Modifier.statusBarsPadding()
                        SnackBarPosition.BOTTOM -> Modifier.navigationBarsPadding()
                    }
                )
        ) { data ->
            val type = (data.visuals as? CustomSnackbarVisuals)?.type ?: SnackBarType.DEFAULT

            Snackbar(
                snackbarData = data,
                shape = ShapeDefaults.Medium,
                containerColor = when (type) {
                    SnackBarType.DEFAULT -> MaterialTheme.colorScheme.inverseSurface
                    SnackBarType.ERROR -> Error
                },
                contentColor = when (type) {
                    SnackBarType.DEFAULT -> MaterialTheme.colorScheme.inverseOnSurface
                    SnackBarType.ERROR -> OnError
                }
            )
        }
    }
}



package com.example.scrollbooker.ui.camera
import androidx.compose.runtime.Composable
import com.example.scrollbooker.components.core.headers.Header

@Composable
fun CameraScreen(
    onBack: () -> Unit
) {
    Header(
        title = "",
        onBack = onBack
    )
}
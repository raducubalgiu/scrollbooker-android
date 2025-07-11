package com.example.scrollbooker.core.util
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity

@Composable
fun isKeyboardOpen(): Boolean {
    val ime = WindowInsets.ime
    val density = LocalDensity.current
    val height = ime.getBottom(density)
    return height > 0
}
package com.example.scrollbooker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = Color.White,

    secondary = Color(0xFFFFC107),
    onSecondary = Color.Black,

    tertiary = Color(0xFF8E24AA),
    onTertiary = Color.White,

    background = Color(0xFFFDFDFD),
    onBackground = Color(0xFF1C1B1F),

    surface = Color(0xFFF1F1F1),
    onSurface = Color(0xFF1C1B1F),

    error = Error,
    onError = onError
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Color.White,

    secondary = Color(0xFFFFB300),
    onSecondary = Color.Black,

    tertiary = Color(0xFF8E24AA),
    onTertiary = Color.White,

    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),

    surface = Color(0xFF1C1C1C),
    onSurface = Color(0xFFAAAAAA),

    error = Error,
    onError = onError
)

@Composable
fun ScrollBookerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
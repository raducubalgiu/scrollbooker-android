package com.example.scrollbooker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF6F00),
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFFFFC107),
    onSecondary = Color.Black,

    tertiary = Color(0xFF8E24AA),
    onTertiary = Color.White,

    background = Color(0xFFFDFDFD),
    onBackground = Color(0xFF1C1B1F),

    surface = Color(0xFFF1F1F1),
    onSurface = Color(0xFF1C1B1F),

    error = Color(0xFFD32F2F),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6F00),
    onPrimary = Color(0xFFE0E0E0),

    secondary = Color(0xFFFFB300),
    onSecondary = Color(0xFFE0E0E0),

    tertiary = Color(0xFF8E24AA),
    onTertiary = Color(0xFFE0E0E0),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),

    surface = Color(0xFF1C1C1C),
    onSurface = Color(0xFFAAAAAA),

    error = Color(0xFFD32F2F),
    onError = Color(0xFFE0E0E0)
)

data class ExtendedColors(
    val divider: Color
)

private val LightExtendedColors = ExtendedColors(
    divider = Color(0xFFCCCCCC)
)
private val DarkExtendedColor = ExtendedColors(
    divider = Color(0xFF3A3A3A)
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(divider = Color.Gray)
}

@Composable
fun ScrollBookerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val extendedColors = if(darkTheme) DarkExtendedColor else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
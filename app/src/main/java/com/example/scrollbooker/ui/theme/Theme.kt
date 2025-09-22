package com.example.scrollbooker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.store.util.ThemePreferenceEnum

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF6F00),
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFFF3BA2F),
    onSecondary = Color.Black,

    tertiary = Color(0xFF607D8B),
    onTertiary = Color(0xFFE0E0E0),

    background = Color(0xFFFDFDFD),
    onBackground = Color(0xFF1C1B1F),

    surface = Color(0xFFF1F1F1),
    onSurface = Color(0xFF1C1B1F),

    error = Color(0xFFD32F2F),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF6F00),
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFFF3BA2F),
    onSecondary = Color(0xFF1C1B1F),

    tertiary = Color(0xFF607D8B),
    onTertiary = Color(0xFF1C1B1F),

    background = Color(0xFF000000),
    onBackground = Color(0xFFE0E0E0),

    surface = Color(0xFF1C1C1C),
    onSurface = Color(0xFFAAAAAA),

    error = Color(0xFFD32F2F),
    onError = Color(0xFFE0E0E0)
)

data class ExtendedColors(
    val divider: Color,
    val lastMinute: Color
)

private val LightExtendedColors = ExtendedColors(
    divider = Color(0xFFCCCCCC),
    lastMinute = Color(0xFF00BCD4)
)
private val DarkExtendedColor = ExtendedColors(
    divider = Color(0xFF3A3A3A),
    lastMinute = Color(0xFF00BCD4)
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        divider = Color.Gray,
        lastMinute = Color(0xFF00BCD4)
    )
}

@Composable
fun ScrollBookerTheme(
    themePreferenceEnum: ThemePreferenceEnum,
    content: @Composable () -> Unit
) {
    val isDarkTheme = when(themePreferenceEnum) {
        ThemePreferenceEnum.SYSTEM -> isSystemInDarkTheme()
        ThemePreferenceEnum.LIGHT -> false
        ThemePreferenceEnum.DARK -> true
    }

    val extendedColors = if(isDarkTheme) DarkExtendedColor else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = if(isDarkTheme) DarkColorScheme else LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}
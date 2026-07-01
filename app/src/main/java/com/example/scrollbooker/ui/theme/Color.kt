package com.example.scrollbooker.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

val Primary: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.primary

val OnPrimary: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.onPrimary

val Secondary: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.secondary

val OnSecondary: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.onSecondary

val Tertiary: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.tertiary

val OnTertiary: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.onTertiary

val Background: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.background

val OnBackground: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.onBackground

val SurfaceBG: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.surface

val OnSurfaceBG: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.onSurface

val Error: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.error

val OnError: Color
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.colorScheme.onError

object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalExtendedColors.current
}

val Divider: Color
    @Composable
    @ReadOnlyComposable
    get() = ExtendedTheme.colors.divider

val LastMinute: Color
    @Composable
    @ReadOnlyComposable
    get() = ExtendedTheme.colors.lastMinute

val BackgroundDark: Color
    @Composable
    @ReadOnlyComposable
    get() = ExtendedTheme.colors.backgroundDark

val BackgroundLight: Color
    @Composable
    @ReadOnlyComposable
    get() = ExtendedTheme.colors.backgroundLight

val Rating: Color
    @Composable
    @ReadOnlyComposable
    get() = ExtendedTheme.colors.rating

val Beauty: Color
    @Composable
    @ReadOnlyComposable
    get() = ExtendedTheme.colors.beauty

val Medical: Color
    @Composable
    @ReadOnlyComposable
    get() = ExtendedTheme.colors.medical

val Auto: Color
    @Composable
    @ReadOnlyComposable
    get() = ExtendedTheme.colors.auto


package com.example.scrollbooker.ui.theme

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary: Color
    @Composable get() = MaterialTheme.colorScheme.primary

val OnPrimary: Color
    @Composable get() = MaterialTheme.colorScheme.onPrimary

val Secondary: Color
    @Composable get() = MaterialTheme.colorScheme.secondary

val OnSecondary: Color
    @Composable get() = MaterialTheme.colorScheme.onSecondary

val Tertiary: Color
    @Composable get() = MaterialTheme.colorScheme.tertiary

val OnTertiary: Color
    @Composable get() = MaterialTheme.colorScheme.onTertiary

val Background: Color
    @Composable get() = MaterialTheme.colorScheme.background

val OnBackground: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground

val SurfaceBG: Color
    @Composable get() = MaterialTheme.colorScheme.surface

val OnSurfaceBG: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface

val Error: Color
    @Composable get() = MaterialTheme.colorScheme.error

val OnError: Color
    @Composable get() = MaterialTheme.colorScheme.onError

object ExtendedTheme {
    val colors: ExtendedColors
        @Composable get() = LocalExtendedColors.current
}

val Divider: Color
    @Composable get() = ExtendedTheme.colors.divider


package com.example.scrollbooker.core.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

fun monochromeGradient(
    base: Color,
    lightFactor: Float = 0.12f,
    darkFactor: Float = 0.06f
): Brush {
    val lighter = lerp(base, Color.White, lightFactor)
    val darker = lerp(base, Color.Black, darkFactor)

    return Brush.linearGradient(
        colors = listOf(lighter, darker),
        start = Offset.Zero,
        end = Offset(1000f, 1000f)
    )
}
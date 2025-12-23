package com.example.scrollbooker.core.extensions

import androidx.compose.ui.graphics.Color

fun Color.withAlpha(
    enabled: Boolean,
    alpha: Float = 0.5f
): Color = if(enabled) copy(alpha = alpha) else this
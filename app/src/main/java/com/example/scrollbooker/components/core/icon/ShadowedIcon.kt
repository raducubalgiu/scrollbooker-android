package com.example.scrollbooker.components.core.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShadowedIcon(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String?,
    iconSize: Dp = 30.dp,
    blurRadius: Dp = 4.dp,
    iconTintColor: Color = Color.White,
    shadowColor: Color = Color.Black.copy(alpha = 0.3f),
    backgroundColor: Color = Color.Transparent,
    xShadowOffset: Dp = 2.dp,
    yShadowOffset: Dp = 2.dp
) {

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = CircleShape)
            .clip(CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(iconSize)
                .offset(x = xShadowOffset, y = yShadowOffset)
                .blur(blurRadius),
            tint = shadowColor,
        )
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painter,
            contentDescription = contentDescription,
            tint = iconTintColor
        )
    }
}
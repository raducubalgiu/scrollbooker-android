package com.example.scrollbooker.components.core.divider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Divider

@Composable
fun VerticalDivider(
    color: Color = Divider,
    height: Dp = 24.dp,
    thickness: Dp = 1.dp
) {
    Box(modifier = Modifier
        .height(height)
        .width(thickness)
        .background(color, shape = RectangleShape)
    )
}
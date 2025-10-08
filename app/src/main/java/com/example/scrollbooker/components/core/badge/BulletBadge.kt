package com.example.scrollbooker.components.core.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Error

@Composable
fun BulletBadge(
    modifier: Modifier = Modifier,
    color: Color = Error
) {
    Box(modifier = modifier.offset(x = 10.dp, y = (-7.5).dp),
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .width(9.dp)
                .height(9.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}
package com.example.scrollbooker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.core.util.Dimens.SpacingM
import com.example.scrollbooker.ui.theme.Primary

@Composable
fun ScreenIndicator(
    modifier: Modifier = Modifier,
    screenSize: Int,
    selectedScreen: Int,
    selectedColor: Color = Primary,
    unselectedColor: Color = Color.Gray.copy(alpha = 0.7f)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(screenSize) { page ->
            val color = if(page == selectedScreen) selectedColor else unselectedColor

            Box(
                modifier = Modifier
                    .padding(end = 2.dp)
                    .size(SpacingM)
                    .clip(CircleShape)
                    .background(color = color)
            )
        }
    }
}
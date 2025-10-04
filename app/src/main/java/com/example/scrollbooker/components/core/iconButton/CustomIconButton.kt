package com.example.scrollbooker.components.core.iconButton

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun CustomIconButton(
    imageVector: ImageVector? = null,
    painter: Int? = null,
    boxSize: Dp = 50.dp,
    iconSize: Dp = 24.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(boxSize)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        imageVector?.let {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = imageVector,
                tint = OnBackground,
                contentDescription = null
            )
        }

        painter?.let {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(painter),
                tint = OnBackground,
                contentDescription = null
            )
        }
    }
}
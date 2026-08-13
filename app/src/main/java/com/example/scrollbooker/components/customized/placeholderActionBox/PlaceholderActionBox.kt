package com.example.scrollbooker.components.customized.placeholderActionBox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.OnSurfaceBG
import com.example.scrollbooker.ui.theme.bodyMedium

@Composable
fun PlaceholderActionBox(
    modifier: Modifier = Modifier,
    description: String,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val alphaFactor = if (enabled) 1f else 0.4f

    val backgroundColor = OnSurfaceBG.copy(alpha = if (enabled) 0.02f else 0.01f)
    val strokeColor = Color.Gray.copy(alpha = if (enabled) 0.6f else 0.25f)
    val contentColor = OnSurfaceBG.copy(alpha = alphaFactor)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val cornerRadius = 16.dp.toPx()
                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)

                drawRoundRect(
                    color = strokeColor,
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = dashEffect
                    ),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            }
            .clickable(enabled = enabled && onClick != null) {
                onClick?.invoke()
            }
            .padding(horizontal = 24.dp, vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Text(
                text = description,
                style = bodyMedium,
                color = contentColor,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}


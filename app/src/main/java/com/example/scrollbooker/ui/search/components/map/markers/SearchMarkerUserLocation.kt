package com.example.scrollbooker.ui.search.components.map.markers

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Path
import kotlin.math.min

@Composable
fun SearchMarkerUserLocation(
    modifier: Modifier = Modifier,
    markerSize: Dp = 50.dp,
    color: Color = Color(0xFF1A73E8),
) {
    Canvas(
        modifier = modifier
            .size(markerSize)
            .shadow(elevation = 4.dp, shape = CircleShape, clip = false)
    ) {
        val w = size.width
        val h = size.height
        val c = center

        val baseRadius = min(w, h) / 2f
        val outerRadius = baseRadius * 0.45f
        val innerRadius = baseRadius * 0.30f

        drawCircle(
            color = Color.White,
            center = c,
            radius = outerRadius
        )

        drawCircle(
            color = color,
            center = c,
            radius = innerRadius
        )

        val coneHeight = baseRadius * 1.8f
        val coneHalfWidth = outerRadius * 1.2f

        val conePath = Path().apply {
            moveTo(c.x, c.y + outerRadius * 0.3f)
            lineTo(c.x - coneHalfWidth, c.y + coneHeight)
            lineTo(c.x + coneHalfWidth, c.y + coneHeight)
            close()
        }

        drawPath(
            path = conePath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    color.copy(alpha = 0.35f),
                    Color.Transparent // dispăre în jos
                ),
                startY = c.y + outerRadius * 0.3f,
                endY = c.y + coneHeight
            )
        )
    }
}
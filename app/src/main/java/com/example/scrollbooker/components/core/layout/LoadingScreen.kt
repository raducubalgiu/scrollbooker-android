package com.example.scrollbooker.components.core.layout

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Primary

private const val SPINNER_SWEEP_ANGLE = 270f
private const val SPINNER_ROTATION_DURATION_MS = 900

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    arrangement: Arrangement.Vertical = Arrangement.Center,
    color: Color = Primary
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = arrangement
    ) {
        FixedArcSpinner(
            modifier = Modifier
                .size(30.dp)
                .then(modifier),
            color = color
        )
    }
}

/**
 * A constant-sweep rotating arc, unlike Material3's [androidx.compose.material3.CircularProgressIndicator]
 * (indeterminate variant), whose arc animates through growing/shrinking phases on every cycle —
 * that phase reads as the spinner "resizing" whenever it's visible long enough to notice (e.g. the
 * cold-start feed load), rather than a genuinely rotating fixed arc.
 */
@Composable
private fun FixedArcSpinner(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Dp = 3.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spinnerRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = SPINNER_ROTATION_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spinnerRotationValue"
    )

    Canvas(modifier = modifier) {
        val stroke = Stroke(width = strokeWidth.toPx())
        val diameter = size.minDimension - stroke.width
        val topLeft = Offset(
            (size.width - diameter) / 2f,
            (size.height - diameter) / 2f
        )

        drawArc(
            color = color,
            startAngle = rotation,
            sweepAngle = SPINNER_SWEEP_ANGLE,
            useCenter = false,
            topLeft = topLeft,
            size = Size(diameter, diameter),
            style = stroke
        )
    }
}

package com.example.scrollbooker.ui.camera.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.ui.theme.Error

@Composable
fun RecordButton(
    modifier: Modifier = Modifier,
    diameter: Dp = 90.dp, // <-- redenumit (era "size")
    ringWidth: Dp = 5.dp,
    innerPadding: Dp = 6.dp,
    colorRed: Color = Error,
    colorRing: Color = Color.White,
    isRecording: Boolean,
    onTap: () -> Unit,
    onLongPress: (() -> Unit)? = null
) {
    // press feedback
    var pressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.94f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "pressScale"
    )

    // pulsing (mereu rulează; când nu înregistrezi, factorul devine 0)
    val infinite = rememberInfiniteTransition(label = "pulse")
    val pulseRaw by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseRaw"
    )
    val pulse = if (isRecording) pulseRaw else 0f

    val ringWidthPx = with(androidx.compose.ui.platform.LocalDensity.current) { ringWidth.toPx() }
    val paddingPx = with(androidx.compose.ui.platform.LocalDensity.current) { innerPadding.toPx() }

    Box(
        modifier = modifier
            .size(diameter)
            .scale(pressScale)
            .semantics {
                role = Role.Button
                contentDescription = if (isRecording) "Stop recording" else "Start recording"
            }
            .clip(CircleShape)
            .drawBehind {
                // Folosim mărimea canvasului, nu parametrul Dp
                val canvasSize = this.size
                val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)

                // umbră discretă
                drawCircle(
                    color = Color(0x33000000),
                    radius = canvasSize.minDimension / 2f,
                    center = center + Offset(0f, canvasSize.minDimension * 0.04f)
                )

                // halo pulsat când înregistrezi
                if (pulse > 0f) {
                    val haloRadius = canvasSize.minDimension / 2f * (1.0f + 0.08f * pulse)
                    drawCircle(
                        color = colorRed.copy(alpha = 0.22f * (0.3f + 0.7f * pulse)),
                        radius = haloRadius,
                        center = center
                    )
                }

                // inel alb
                drawCircle(
                    color = colorRing,
                    radius = canvasSize.minDimension / 2f - ringWidthPx / 2f,
                    center = center,
                    style = Stroke(width = ringWidthPx)
                )

                // disc roșu interior
                val innerRadius = canvasSize.minDimension / 2f - ringWidthPx - paddingPx
                drawCircle(
                    color = colorRed,
                    radius = innerRadius,
                    center = center
                )
            }
            .pointerInput(isRecording) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        try {
                            tryAwaitRelease()
                        } finally {
                            pressed = false
                        }
                    },
                    onTap = { onTap() },
                    onLongPress = { onLongPress?.invoke() }
                )
            }
    )
}

/* --- previews --- */

@Preview
@Composable
private fun RecordButtonIdlePreview() {
    Surface(color = Color(0xFFF2F2F2)) {
        RecordButton(isRecording = false, onTap = {}, onLongPress = {})
    }
}

@Preview
@Composable
private fun RecordButtonRecordingPreview() {
    Surface(color = Color(0xFFF2F2F2)) {
        RecordButton(isRecording = true, onTap = {}, onLongPress = {})
    }
}

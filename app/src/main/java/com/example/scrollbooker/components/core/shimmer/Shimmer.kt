package com.example.scrollbooker.components.core.shimmer
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.ui.theme.Divider
import androidx.compose.runtime.getValue

enum class ShimmerMode {
    SYSTEM, LIGHT, DARK
}

fun Modifier.shimmerEffect(
    mode: ShimmerMode = ShimmerMode.SYSTEM
): Modifier = composed {
    val color = when(mode) {
        ShimmerMode.SYSTEM -> Divider
        ShimmerMode.LIGHT -> Color(0xFFCCCCCC)
        ShimmerMode.DARK -> Color(0xFF3A3A3A)
    }

    val shimmerColors = listOf(
        color.copy(alpha = 0.5f),
        color.copy(alpha = 0.2f),
        color.copy(alpha = 0.5f)
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        ),
        label = "shimmer_translation"
    )

    this.drawWithCache {
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnim, y = translateAnim),
            end = Offset(x = translateAnim + 300f, y = translateAnim + 300f)
        )
        onDrawWithContent {
            drawRect(brush = brush)
        }
    }
}
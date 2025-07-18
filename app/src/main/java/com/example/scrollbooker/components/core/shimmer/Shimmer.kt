package com.example.scrollbooker.components.core.shimmer
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.scrollbooker.ui.theme.Divider
import com.example.scrollbooker.ui.theme.SurfaceBG

enum class ShimmerMode {
    SYSTEM,
    LIGHT,
    DARK
}

@Composable
fun rememberShimmerBrush(
    mode: ShimmerMode = ShimmerMode.SYSTEM
): Brush {
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

    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
}
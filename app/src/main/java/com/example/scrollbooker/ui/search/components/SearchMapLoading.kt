package com.example.scrollbooker.ui.search.components
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.scrollbooker.ui.theme.Background
import com.example.scrollbooker.ui.theme.OnBackground

@Composable
fun SearchMapLoading(
    modifier: Modifier = Modifier,
    dotSize: Dp = 9.dp,
    spaceBetween: Dp = 6.dp,
    animationDelay: Int = 140
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")

    val colors = List(3) { i ->
        infiniteTransition.animateColor(
            initialValue = Background,
            targetValue = OnBackground,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(i * animationDelay)
            ),
            label = "dot$i"
        )
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.shadow(
            elevation = 2.dp,
            shape = CircleShape,
            clip = false
        )) {
            Row(
                modifier = modifier
                    .background(Background, shape = RoundedCornerShape(50.dp))
                    .padding(horizontal = 18.dp, vertical = 16.dp)
                    .zIndex(5f),
                horizontalArrangement = Arrangement.spacedBy(spaceBetween),
                verticalAlignment = Alignment.CenterVertically
            ) {
                colors.forEach { a ->
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .background(a.value, shape = CircleShape)
                    )
                }
            }
        }
    }
}
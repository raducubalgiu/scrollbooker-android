package com.example.scrollbooker.components.customized.post.components
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.example.scrollbooker.R
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.bodyLarge

private const val MaxRevealFraction = 0.1f

@Composable
fun EndOfFeedPager(
    pagerState: PagerState,
    isAtLastPage: () -> Boolean,
    modifier: Modifier = Modifier,
    pager: @Composable (pagerModifier: Modifier) -> Unit
) {
    val density = LocalDensity.current
    val currentIsAtLastPage = rememberUpdatedState(isAtLastPage)

    var viewportHeightPx by remember { mutableIntStateOf(0) }
    var overscrollPx by remember { mutableFloatStateOf(0f) }

    val connection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val maxOverscrollPx = viewportHeightPx * MaxRevealFraction
                if (available.y < 0f && maxOverscrollPx > 0f && currentIsAtLastPage.value()) {
                    val newValue = (overscrollPx - available.y).coerceIn(0f, maxOverscrollPx)
                    val consumedAmount = newValue - overscrollPx
                    overscrollPx = newValue
                    return Offset(0f, -consumedAmount)
                }
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }.collect { inProgress ->
            if (!inProgress && overscrollPx > 0f) {
                animate(
                    initialValue = overscrollPx,
                    targetValue = 0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) { value, _ -> overscrollPx = value }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .onSizeChanged { viewportHeightPx = it.height }
    ) {
        val maxOverscrollPx = viewportHeightPx * MaxRevealFraction
        if (maxOverscrollPx > 0f) {
            EndOfFeedHint(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(with(density) { maxOverscrollPx.toDp() })
            )
        }

        pager(
            Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = -overscrollPx }
                .nestedScroll(connection)
        )
    }
}

@Composable
private fun EndOfFeedHint(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.noMorePosts),
                style = bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

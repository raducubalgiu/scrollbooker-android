package com.example.scrollbooker.components.customized.post.components
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.scrollbooker.R
import com.example.scrollbooker.core.util.Dimens.SpacingS
import com.example.scrollbooker.ui.theme.BackgroundDark
import com.example.scrollbooker.ui.theme.bodyLarge

/** How much of the viewport the "no more posts" card is allowed to reveal, as a
 * fraction of the pager's height — i.e. how far past its last page it rubber-bands. */
private const val MaxRevealFraction = 0.25f

/**
 * Wraps a TikTok-style [androidx.compose.foundation.pager.VerticalPager] so that
 * swiping past its last page rubber-bands: the current (last) page slides up,
 * revealing a fixed "Nu mai sunt postări" card peeking in from the bottom — as if
 * it were one more, shorter item in the list — and springs back to fully cover
 * the card the moment the user lets go, without ever actually changing page.
 *
 * [pagerState] is only used to know when the drag/fling has ended (to trigger the
 * snap-back); [isAtLastPage] decides whether an upward drag is eligible to reveal
 * the card at all. [pager] receives the modifier that must be applied to the
 * actual `VerticalPager` (it carries both the reveal translation and the nested
 * scroll connection driving it).
 */
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
                // Negative y: still dragging "forward" (up) after the pager itself
                // couldn't consume any more, i.e. already on its last page.
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

    // Snap back the moment the gesture ends, regardless of how it ended (release,
    // fling, or the pager settling back onto its own page) — driven off the
    // pager's own scroll-in-progress state rather than a fling callback, since a
    // snapping pager's fling handling isn't guaranteed to look like a plain list's.
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
            Icon(
                painter = painterResource(R.drawable.ic_video_outline),
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(SpacingS))
            Text(
                text = stringResource(R.string.noMorePosts),
                style = bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

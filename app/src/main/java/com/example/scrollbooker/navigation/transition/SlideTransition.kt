package com.example.scrollbooker.navigation.transition
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

internal const val TRANSITION_DURATION = 300
internal val PremiumNavigationEasing = CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)

fun AnimatedContentTransitionScope<*>.slideInFromRight(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(TRANSITION_DURATION, easing = PremiumNavigationEasing)
    )
}

fun AnimatedContentTransitionScope<*>.slideOutToLeft(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth / 4 },
        animationSpec = tween(TRANSITION_DURATION, easing = PremiumNavigationEasing)
    )
}

fun AnimatedContentTransitionScope<*>.slideInFromLeft(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth / 4 },
        animationSpec = tween(TRANSITION_DURATION, easing = PremiumNavigationEasing)
    )
}

fun AnimatedContentTransitionScope<*>.slideOutToRight(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth },
        animationSpec = tween(TRANSITION_DURATION, easing = PremiumNavigationEasing)
    )
}
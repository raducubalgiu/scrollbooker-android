package com.example.scrollbooker.navigation.transition
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween

private const val TRANSITION_DURATION = 300

fun AnimatedContentTransitionScope<*>.slideInFromRight(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(
            durationMillis = TRANSITION_DURATION,
            easing = LinearOutSlowInEasing
        )
    )
}

fun AnimatedContentTransitionScope<*>.slideOutToLeft(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(
            durationMillis = TRANSITION_DURATION,
            easing = LinearOutSlowInEasing
        )
    )
}

fun AnimatedContentTransitionScope<*>.slideInFromLeft(): EnterTransition {
    return slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(
            durationMillis = TRANSITION_DURATION,
            easing = LinearOutSlowInEasing
        )
    )
}

fun AnimatedContentTransitionScope<*>.slideOutToRight(): ExitTransition {
    return slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(
            durationMillis = TRANSITION_DURATION,
            easing = LinearOutSlowInEasing
        )
    )
}
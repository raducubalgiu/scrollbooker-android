package com.example.scrollbooker.core.nav.transitions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween

fun slideEnterTransition(
    direction: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Left,
    duration: Int = 300
): AnimatedContentTransitionScope<*>.() -> EnterTransition {
    return {
        slideIntoContainer(
            direction,
            animationSpec = tween(duration)
        )
    }
}

fun slideExitTransition(
    direction: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Right,
    duration: Int = 300
): AnimatedContentTransitionScope<*>.() -> ExitTransition {
    return {
        slideOutOfContainer(
            direction,
            animationSpec = tween(duration)
        )
    }
}
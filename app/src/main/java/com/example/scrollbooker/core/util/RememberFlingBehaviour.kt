package com.example.scrollbooker.core.util

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberFlingBehavior(
    maxFlingVelocity: Float = 5500f
): FlingBehavior {
    val decay = rememberSplineBasedDecay<Float>()

    return remember(maxFlingVelocity, decay) {
        object : FlingBehavior {
            override suspend fun ScrollScope.performFling(
                initialVelocity: Float
            ): Float {

                val v0 = initialVelocity.coerceIn(
                    -maxFlingVelocity,
                    maxFlingVelocity
                )

                if (kotlin.math.abs(v0) < 1f) return v0

                var velocityLeft = v0
                var lastValue = 0f

                val state = AnimationState(
                    initialValue = 0f,
                    initialVelocity = v0
                )

                state.animateDecay(decay) {
                    val delta = value - lastValue
                    lastValue = value

                    val consumed = scrollBy(delta)

                    if (kotlin.math.abs(delta - consumed) > 0.5f) {
                        cancelAnimation()
                    }

                    velocityLeft = this.velocity
                }

                return velocityLeft
            }
        }
    }
}
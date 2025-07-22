package com.example.scrollbooker.ui.profile.myProfile.myBusiness.myCalendar

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import androidx.compose.runtime.getValue

@Composable
fun rememberAnimatedPagerStyle(
    pagerState: PagerState,
    index: Int,
    primaryColor: Color
): Triple<Color, Float, Dp> {
    val offsetFraction = pagerState.currentPageOffsetFraction
    val currentPage = pagerState.currentPage

    val isCurrent = index == currentPage
    val isNext = index == currentPage + 1
    val isPrevious = index == currentPage - 1

    val targetColor = when {
        isCurrent && offsetFraction == 0f -> primaryColor
        isCurrent && offsetFraction != 0f -> primaryColor.copy(alpha = 0.9f)
        (isNext || isPrevious) && offsetFraction.absoluteValue > 0.5f -> primaryColor.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    val targetScale = when {
        isCurrent && offsetFraction == 0f -> 1f
        isCurrent && offsetFraction != 0f -> 0.95f
        (isNext || isPrevious) && offsetFraction.absoluteValue > 0.5f -> 1f
        else -> 1f
    }

    val targetElevation = when {
        isCurrent && offsetFraction == 0f -> 4.dp
        isCurrent && offsetFraction != 0f -> 1.dp
        else -> 0.dp
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(150),
        label = "AnimatedDayColor"
    )

    val animatedScale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(150),
        label = "AnimatedDayScale"
    )

    val animatedElevation by animateDpAsState(
        targetValue = targetElevation,
        animationSpec = tween(250),
        label = "AnimatedDayElevation"
    )

    return Triple(
        animatedColor,
        animatedScale,
        animatedElevation
    )
}
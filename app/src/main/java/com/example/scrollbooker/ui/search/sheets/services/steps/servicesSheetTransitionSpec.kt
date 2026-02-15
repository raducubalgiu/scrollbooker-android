package com.example.scrollbooker.ui.search.sheets.services.steps

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import com.example.scrollbooker.ui.search.sheets.services.ServicesSheetStep

fun servicesSheetTransitionSpec(
    mainStep: ServicesSheetStep = ServicesSheetStep.MAIN_FILTERS,
    clip: Boolean = false,
): AnimatedContentTransitionScope<ServicesSheetStep>.() -> ContentTransform = {
    val isForward = targetState != mainStep

    val enter = if (isForward) {
        slideInHorizontally { fullWidth -> fullWidth }
    } else {
        slideInHorizontally { fullWidth -> -fullWidth }
    } + fadeIn()

    val exit = if (isForward) {
        slideOutHorizontally { fullWidth -> -fullWidth }
    } else {
        slideOutHorizontally { fullWidth -> fullWidth }
    } + fadeOut()

    enter togetherWith exit using SizeTransform(clip = clip)
}
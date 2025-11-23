package com.example.scrollbooker.ui

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.StateFlow

data class LocationController(
    val stateFlow: StateFlow<LocationState>,
    val startUpdates: (PrecisionMode) -> Unit,
    val stopUpdates: () -> Unit,
    val onPermissionResult: (Boolean) -> Unit
)

val LocalLocationController = staticCompositionLocalOf<LocationController> {
    error("LocalLocationController not provided")
}
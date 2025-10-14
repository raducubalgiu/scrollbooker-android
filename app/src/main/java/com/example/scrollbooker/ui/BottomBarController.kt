package com.example.scrollbooker.ui

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class BottomBarController(
    val appointments: StateFlow<Int>,
    val notifications: StateFlow<Int>,
    val incAppointments: () -> Unit = {},
    val decAppointments: () -> Unit = {},
    val setAppointments: (Int) -> Unit = {},
    val setNotifications: (Int) -> Unit = {}
)

val LocalBottomBarController = staticCompositionLocalOf {
    BottomBarController(
        appointments = MutableStateFlow(0),
        notifications = MutableStateFlow(0)
    )
}
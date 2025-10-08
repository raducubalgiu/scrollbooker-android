package com.example.scrollbooker.navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.scrollbooker.navigation.bottomBar.MainTab
import kotlinx.coroutines.flow.StateFlow

@Stable
data class TabsController(
    val currentTab: StateFlow<MainTab>,
    val setTab: (MainTab) -> Unit
)

val LocalTabsController = staticCompositionLocalOf<TabsController> {
    error("LocalTabsController not provided")
}
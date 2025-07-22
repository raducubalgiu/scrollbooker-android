package com.example.scrollbooker.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalRootNavController = staticCompositionLocalOf<NavHostController> {
    error("Root NavController Not Provided")
}
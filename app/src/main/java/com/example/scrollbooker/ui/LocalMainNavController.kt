package com.example.scrollbooker.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalMainNavController = staticCompositionLocalOf<NavHostController> {
    error("Main NavController Not Provided")
}
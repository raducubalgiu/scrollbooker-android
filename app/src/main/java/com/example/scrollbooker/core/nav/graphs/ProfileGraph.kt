package com.example.scrollbooker.core.nav.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.profile.presentation.ProfileScreen
import com.example.scrollbooker.feature.settings.presentation.SettingsScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation(
        route = MainRoute.ProfileNavigator.route,
        startDestination = MainRoute.Profile.route,
    ) {
        composable(
            MainRoute.Profile.route,
        ) {
            ProfileScreen(navController = navController)
        }

        composable(
            MainRoute.Settings.route,
        ) {
            SettingsScreen(navController = navController)
        }
    }
}
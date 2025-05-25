package com.example.scrollbooker.core.nav.navigators

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute

fun NavGraphBuilder.profileNavigator(navController: NavController) {
    navigation(
        route = MainRoute.ProfileRootNavigator.route,
        startDestination = MainRoute.Profile.route,
    ) {
        profileGraph(navController)
        myBusinessGraph(navController)
        settingsGraph(navController)
    }
}
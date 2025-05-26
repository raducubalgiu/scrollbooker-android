package com.example.scrollbooker.core.nav.navigators

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideInFromLeft
import com.example.scrollbooker.core.nav.transitions.slideInFromRight
import com.example.scrollbooker.core.nav.transitions.slideOutToLeft
import com.example.scrollbooker.core.nav.transitions.slideOutToRight
import com.example.scrollbooker.feature.profile.editProfile.presentation.EditFullNameScreen
import com.example.scrollbooker.feature.profile.editProfile.presentation.EditProfileScreen
import com.example.scrollbooker.feature.profile.root.presentation.ProfileScreen

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation(
        route = MainRoute.ProfileNavigator.route,
        startDestination = MainRoute.Profile.route,
    ) {
        composable(MainRoute.Profile.route) {
            ProfileScreen(navController)
        }
        composable(
            MainRoute.EditProfile.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            EditProfileScreen(navController)
        }

        composable(
            MainRoute.EditFullName.route,
            enterTransition = slideInFromRight(),
            exitTransition = slideOutToLeft(),
            popEnterTransition = slideInFromLeft(),
            popExitTransition = slideOutToRight()
        ) {
            EditFullNameScreen(navController)
        }
    }
}
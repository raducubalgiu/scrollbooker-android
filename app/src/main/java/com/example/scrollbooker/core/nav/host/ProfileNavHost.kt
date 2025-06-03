package com.example.scrollbooker.core.nav.host

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.profile.presentation.ProfileScreen
import com.example.scrollbooker.feature.profile.presentation.ProfileSharedViewModel

@Composable
fun ProfileNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Profile.route
    ) {
        composable(MainRoute.Profile.route) { backStackEntry ->
            val viewModel = hiltViewModel<ProfileSharedViewModel>(backStackEntry)
            ProfileScreen(viewModel = viewModel)
        }
    }
}
package com.example.scrollbooker.core.nav.host

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.LocalRootNavController
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.core.nav.transitions.slideEnterTransition
import com.example.scrollbooker.core.nav.transitions.slideExitTransition
import com.example.scrollbooker.screens.auth.presentation.AuthViewModel
import com.example.scrollbooker.screens.auth.presentation.LoginScreen
import com.example.scrollbooker.screens.auth.presentation.RegisterScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.CollectBusinessLocationScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.CollectBusinessSchedulesScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.CollectBusinessServicesScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectClientDetails.CollectBirthDateScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectClientDetails.CollectUsernameScreen

@Composable
fun AuthNavHost(viewModel: AuthViewModel) {
    val navController = rememberNavController()

    Box(
        modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()
    ) {
        NavHost(
            navController = navController,
            startDestination = AuthRoute.Login.route
        ) {
            composable(AuthRoute.Login.route) {
                LoginScreen(
                    authNavController = navController,
                    rootNavController = LocalRootNavController.current,
                    viewModel = viewModel
                )
            }
            composable(AuthRoute.Register.route) {
                RegisterScreen(navController)
            }

            // Client Auth Onboarding
            composable(AuthRoute.Username.route) {
                CollectUsernameScreen(navController)
            }
            composable(AuthRoute.BirthDate.route) {
                CollectBirthDateScreen(navController)
            }

            // Business Auth OnBoarding
            composable(route = AuthRoute.BusinessLocation.route,
                enterTransition = slideEnterTransition(),
                popExitTransition = slideExitTransition()
            ) {
                CollectBusinessLocationScreen(navController)
            }

            composable(AuthRoute.BusinessServices.route,
                enterTransition = slideEnterTransition(),
                popExitTransition = slideExitTransition()
            ) {
                CollectBusinessServicesScreen(navController)
            }

            composable(AuthRoute.BusinessSchedules.route,
                enterTransition = slideEnterTransition(),
                popExitTransition = slideExitTransition()
            ) {
                CollectBusinessSchedulesScreen(navController)
            }
        }
    }
}
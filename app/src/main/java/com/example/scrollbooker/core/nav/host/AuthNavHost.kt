package com.example.scrollbooker.core.nav.host

import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.MainViewModel
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.feature.auth.presentation.LoginScreen
import com.example.scrollbooker.feature.auth.presentation.RegisterScreen
import com.example.scrollbooker.feature.auth.presentation.collectBusinessDetails.CollectBusinessLocationScreen
import com.example.scrollbooker.feature.auth.presentation.collectBusinessDetails.CollectBusinessSchedulesScreen
import com.example.scrollbooker.feature.auth.presentation.collectBusinessDetails.CollectBusinessServicesScreen
import com.example.scrollbooker.feature.auth.presentation.collectClientDetails.CollectBirthDateScreen
import com.example.scrollbooker.feature.auth.presentation.collectClientDetails.CollectUsernameScreen

@Composable
fun AuthNavHost(viewModel: MainViewModel) {
    val navController = rememberNavController()

    Box(
        modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .safeDrawingPadding()
    ) {
        NavHost(
            navController = navController,
            startDestination = AuthRoute.Login.route
        ) {

            composable(route = AuthRoute.Login.route) {
                LoginScreen(navController = navController)
            }
            composable(route = AuthRoute.Register.route) {
                RegisterScreen(
                    navController = navController,
                    onRegisterSuccess = {}
                )
            }

            // Client Auth Onboarding
            composable(
                route = AuthRoute.Username.route,
            ) {
                CollectUsernameScreen(navController = navController)
            }
            composable(
                route = AuthRoute.BirthDate.route
            ) {
                CollectBirthDateScreen(navController = navController)
            }

            // Business Auth OnBoarding
            composable(
                route = AuthRoute.BusinessLocation.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) }
            ) {
                CollectBusinessLocationScreen(navController = navController)
            }

            composable(
                route = AuthRoute.BusinessServices.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) }
            ) {
                CollectBusinessServicesScreen(navController = navController)
            }

            composable(
                route = AuthRoute.BusinessSchedules.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) }
            ) {
                CollectBusinessSchedulesScreen(navController = navController)
            }
        }
    }
}
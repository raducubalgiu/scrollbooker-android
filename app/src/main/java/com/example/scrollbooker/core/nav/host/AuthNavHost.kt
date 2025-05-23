package com.example.scrollbooker.core.nav.host

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
fun AuthNavHost(
    navController: NavController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = rememberNavController(),
        startDestination = AuthRoute.Login.route
    ) {
        composable(route = AuthRoute.Login.route) {
            LoginScreen(onLoginSuccess = {})
        }
        composable(route = AuthRoute.Register.route) {
            RegisterScreen(navController = navController, onRegisterSuccess = {})
        }

        // Client Auth Onboarding
        composable(route = AuthRoute.Username.route) {
            CollectUsernameScreen(navController = navController)
        }
        composable(route = AuthRoute.BirthDate.route) {
            CollectBirthDateScreen(navController = navController)
        }

        // Business Auth OnBoarding
        composable(route = AuthRoute.BirthDate.route) {
            CollectBirthDateScreen(navController = navController)
        }

        composable(route = AuthRoute.BusinessLocation.route) {
            CollectBusinessLocationScreen(navController = navController)
        }

        composable(route = AuthRoute.BusinessServices.route) {
            CollectBusinessServicesScreen(navController = navController)
        }

        composable(route = AuthRoute.BusinessSchedules.route) {
            CollectBusinessSchedulesScreen(navController = navController)
        }
    }
}
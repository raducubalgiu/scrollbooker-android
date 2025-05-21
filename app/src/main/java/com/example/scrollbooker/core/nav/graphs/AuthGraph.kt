package com.example.scrollbooker.core.nav.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.core.nav.routes.GlobalRoute
import com.example.scrollbooker.feature.auth.presentation.LoginScreen
import com.example.scrollbooker.feature.auth.presentation.RegisterScreen
import com.example.scrollbooker.feature.auth.presentation.collectClientDetails.CollectBirthDateScreen
import com.example.scrollbooker.feature.auth.presentation.collectClientDetails.CollectUsernameScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        route = GlobalRoute.AUTH,
        startDestination = AuthRoute.Login.route
    ) {
        composable(route = AuthRoute.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AuthRoute.Register.route) {
            RegisterScreen(navController = navController, onRegisterSuccess = {})
        }
        composable(route = AuthRoute.Username.route) {
            CollectUsernameScreen(navController = navController)
        }
        composable(route = AuthRoute.BirthDate.route) {
            CollectBirthDateScreen(navController = navController)
        }
    }
}
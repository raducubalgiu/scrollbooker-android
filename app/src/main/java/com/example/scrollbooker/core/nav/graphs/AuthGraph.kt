package com.example.scrollbooker.core.nav.graphs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.core.nav.routes.GlobalRoute
import com.example.scrollbooker.feature.auth.presentation.LoginScreen
import com.example.scrollbooker.feature.auth.presentation.RegisterScreen

fun NavGraphBuilder.authGraph(navController: NavController) {
    navigation(
        route = GlobalRoute.AUTH,
        startDestination = AuthRoute.Login.route
    ) {
        composable(route = AuthRoute.Login.route) {
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                LoginScreen(navController = navController, onLoginSuccess = {})
            }
        }
        composable(route = AuthRoute.Register.route) {
            RegisterScreen(navController = navController, onRegisterSuccess = {})
        }
    }
}
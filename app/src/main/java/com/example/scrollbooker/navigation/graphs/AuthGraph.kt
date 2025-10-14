package com.example.scrollbooker.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.scrollbooker.entity.auth.data.remote.RoleNameEnum
import com.example.scrollbooker.navigation.routes.AuthRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.auth.AuthViewModel
import com.example.scrollbooker.ui.auth.LoginScreen
import com.example.scrollbooker.ui.auth.RegisterBusinessScreen
import com.example.scrollbooker.ui.auth.RegisterClientScreen

fun NavGraphBuilder.authGraph(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    composable(AuthRoute.Login.route) {
        LoginScreen(
            viewModel = authViewModel,
            onNavigateToRegisterClient = { navController.navigate(AuthRoute.RegisterClient.route) },
            onNavigateToRegisterBusiness = { navController.navigate(AuthRoute.RegisterBusiness.route) },
            onSubmit = { username, password ->
                authViewModel.login(
                    username,
                    password
                )
            }
        )
    }

    composable(AuthRoute.RegisterClient.route) {
        RegisterClientScreen(
            viewModel = authViewModel,
            onNavigateToLogin = { navController.navigate(AuthRoute.Login.route) },
            onNavigateToRegisterBusiness = { navController.navigate(AuthRoute.RegisterBusiness.route) },
            onSubmit = { email, password ->
                authViewModel.register(
                    email,
                    password,
                    roleName = RoleNameEnum.CLIENT
                )
            }
        )
    }

    composable(
        AuthRoute.RegisterBusiness.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        RegisterBusinessScreen(
            viewModel = authViewModel,
            onBack = { navController.popBackStack() },
            onSubmit = { email, password ->
                authViewModel.register(
                    email,
                    password,
                    roleName = RoleNameEnum.BUSINESS
                )
            }
        )
    }
}
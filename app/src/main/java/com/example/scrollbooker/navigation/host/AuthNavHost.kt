package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
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
import com.example.scrollbooker.ui.onboarding.shared.CollectEmailVerificationScreen

@Composable
fun AuthNavHost(
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute.Login.route
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

        composable(AuthRoute.CollectEmailVerification.route) {
            CollectEmailVerificationScreen(
                onNext = {
                    authViewModel.verifyEmail()
                    navController.navigate(AuthRoute.CollectUserUsername.route)
                },
            )
        }
    }
}
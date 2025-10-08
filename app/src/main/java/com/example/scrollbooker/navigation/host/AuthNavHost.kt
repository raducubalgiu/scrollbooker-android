package com.example.scrollbooker.navigation.host
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.ui.theme.Background
import androidx.compose.runtime.getValue
import com.example.scrollbooker.core.util.FeatureState
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
fun AuthNavHost(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.collectAsState()

    val startDestination = when(val state = authState) {
        is FeatureState.Success -> {
            val isValidated = state.data.isValidated
            val registrationStep = state.data.registrationStep?.key

            if(!isValidated && registrationStep != null) {
                registrationStep
            } else {
                AuthRoute.Login.route
            }
        }
        else -> AuthRoute.Login.route
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Background)
        .statusBarsPadding()
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination
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
}
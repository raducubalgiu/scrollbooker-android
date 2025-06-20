package com.example.scrollbooker.core.nav.host

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scrollbooker.core.nav.LocalRootNavController
import com.example.scrollbooker.core.nav.routes.AuthRoute
import com.example.scrollbooker.screens.auth.presentation.AuthViewModel
import com.example.scrollbooker.screens.auth.presentation.LoginScreen
import com.example.scrollbooker.screens.auth.presentation.RegisterScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessLocation.CollectBusinessLocationScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessSchedules.CollectBusinessSchedulesScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessServices.CollectBusinessServicesScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessType.CollectBusinessTypeScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectClientDetails.CollectBirthDateScreen
import com.example.scrollbooker.screens.auth.presentation.components.collectClientDetails.CollectUsernameScreen
import androidx.navigation.compose.navigation
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessLocation.CollectBusinessLocationViewModel
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessSchedules.CollectBusinessSchedulesViewModel
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessServices.CollectBusinessServicesViewModel
import com.example.scrollbooker.screens.auth.presentation.components.collectBusinessDetails.collectBusinessType.CollectBusinessTypeViewModel

@Composable
fun AuthNavHost(viewModel: AuthViewModel) {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
        NavHost(
            navController = navController,
            startDestination = AuthRoute.Login.route
        ) {
            composable(AuthRoute.Login.route) {
                LoginScreen(
                    authNavController = navController,
                    rootNavController = LocalRootNavController.current,
                    viewModel = viewModel,
                    onNavigateBusinessType = { navController.navigate(AuthRoute.CollectBusinessType.route) }
                )
            }

            composable(AuthRoute.Register.route) {
                RegisterScreen(navController)
            }

            composable(AuthRoute.Username.route) {
                CollectUsernameScreen(navController)
            }
            composable(AuthRoute.BirthDate.route) {
                CollectBirthDateScreen(navController)
            }

            navigation(
                route = AuthRoute.CollectBusinessNavigator.route,
                startDestination = AuthRoute.CollectBusinessType.route,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            ) {
                composable(route = AuthRoute.CollectBusinessType.route) { backStackEntry ->
                    val viewModel: CollectBusinessTypeViewModel = hiltViewModel(backStackEntry)

                    CollectBusinessTypeScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNext = { navController.navigate(AuthRoute.CollectBusinessLocation.route) }
                    )
                }

                composable(route = AuthRoute.CollectBusinessLocation.route) { backStackEntry ->
                    val viewModel: CollectBusinessLocationViewModel = hiltViewModel(backStackEntry)

                    CollectBusinessLocationScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNext = { navController.navigate(AuthRoute.CollectBusinessServices.route) }
                    )
                }

                composable(AuthRoute.CollectBusinessServices.route) { backStackEntry ->
                    val viewModel: CollectBusinessServicesViewModel = hiltViewModel(backStackEntry)

                    CollectBusinessServicesScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNext = { navController.navigate(AuthRoute.CollectBusinessSchedules.route) }
                    )
                }

                composable(AuthRoute.CollectBusinessSchedules.route) { backStackEntry ->
                    val viewModel: CollectBusinessSchedulesViewModel = hiltViewModel(backStackEntry)

                    CollectBusinessSchedulesScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onNext = {  }
                    )
                }
            }
        }
    }
}
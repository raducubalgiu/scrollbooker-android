package com.example.scrollbooker.core.nav.host

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.screens.main.MainUIViewModel
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.screens.appointments.AppointmentDetailsScreen
import com.example.scrollbooker.screens.appointments.AppointmentsScreen
import com.example.scrollbooker.screens.appointments.AppointmentsViewModel
import com.example.scrollbooker.screens.appointments.AppointmentCancelScreen
import kotlinx.coroutines.launch

@Composable
fun AppointmentsNavHost(
    navController: NavHostController,
    mainViewModel: MainUIViewModel
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.AppointmentsNavigator.route,
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
        navigation(
            route = MainRoute.AppointmentsNavigator.route,
            startDestination = MainRoute.Appointments.route,
        ) {
            composable(MainRoute.Appointments.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.AppointmentsNavigator.route)
                }

                val viewModel = hiltViewModel<AppointmentsViewModel>(parentEntry)

                AppointmentsScreen(
                    viewModel = viewModel,
                    navigateToAppointmentDetails = {
                        viewModel.setAppointment(it)
                        navController.navigate(MainRoute.AppointmentDetails.route)
                    }
                )
            }

            composable(MainRoute.AppointmentDetails.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.AppointmentsNavigator.route)
                }
                val viewModel = hiltViewModel<AppointmentsViewModel>(parentEntry)

                AppointmentDetailsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onGoToCancel = { navController.navigate(MainRoute.AppointmentCancel.route) }
                )
            }

            composable(MainRoute.AppointmentCancel.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(MainRoute.AppointmentsNavigator.route)
                }
                val viewModel = hiltViewModel<AppointmentsViewModel>(parentEntry)

                AppointmentCancelScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onCancelAppointment = { appointmentId, message ->
                        navController.currentBackStackEntry?.lifecycleScope?.launch {
                            val result = viewModel.cancelAppointment(appointmentId, message)

                            result
                                .onSuccess {
                                    mainViewModel.decreaseAppointmentsNumber()
                                    navController.popBackStack()
                                }
                                .onFailure {  }
                        }
                    }
                )
            }
        }
    }
}
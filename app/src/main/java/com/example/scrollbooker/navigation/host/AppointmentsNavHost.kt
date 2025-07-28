package com.example.scrollbooker.navigation.host
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.appointments.AppointmentCancelScreen
import com.example.scrollbooker.ui.appointments.AppointmentDetailsScreen
import com.example.scrollbooker.ui.appointments.AppointmentsScreen
import com.example.scrollbooker.ui.appointments.AppointmentsViewModel
import com.example.scrollbooker.ui.main.MainUIViewModel
import kotlinx.coroutines.launch

@Composable
fun AppointmentsNavHost(
    navController: NavHostController,
    mainViewModel: MainUIViewModel
) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.AppointmentsNavigator.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
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
                        }
                    }
                )
            }
        }
    }
}
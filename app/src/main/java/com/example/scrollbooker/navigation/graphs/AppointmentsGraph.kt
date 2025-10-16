package com.example.scrollbooker.navigation.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.scrollbooker.navigation.routes.MainRoute
import com.example.scrollbooker.navigation.transition.slideInFromLeft
import com.example.scrollbooker.navigation.transition.slideInFromRight
import com.example.scrollbooker.navigation.transition.slideOutToLeft
import com.example.scrollbooker.navigation.transition.slideOutToRight
import com.example.scrollbooker.ui.LocalMainNavController
import com.example.scrollbooker.ui.appointments.AppointmentCancelScreen
import com.example.scrollbooker.ui.appointments.AppointmentDetailsScreen
import com.example.scrollbooker.ui.appointments.AppointmentsScreen
import com.example.scrollbooker.ui.appointments.AppointmentsViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

fun NavGraphBuilder.appointmentsGraph(
    navController: NavHostController,
    appointmentCreated: Boolean
) {
    navigation(
        route = MainRoute.AppointmentsNavigator.route,
        startDestination = MainRoute.Appointments.route,
        enterTransition = { slideInFromRight() },
        exitTransition = { slideOutToLeft() },
        popEnterTransition = { slideInFromLeft() },
        popExitTransition = { slideOutToRight() }
    ) {
        composable(route = MainRoute.Appointments.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.AppointmentsNavigator.route)
            }

            val viewModel = hiltViewModel<AppointmentsViewModel>(parentEntry)

            LaunchedEffect(appointmentCreated) {
                if(appointmentCreated) {
                    viewModel.refreshAppointments()
                    parentEntry.savedStateHandle["APPOINTMENT_CREATED"] = false
                }
            }

            AppointmentsScreen(
                viewModel = viewModel,
                onNavigateToAppointmentDetails = {
                    viewModel.setAppointment(it)
                    navController.navigate(MainRoute.AppointmentDetails.route)
                }
            )
        }

        composable(route = MainRoute.AppointmentDetails.route) { backStackEntry ->
            val mainNavController = LocalMainNavController.current

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MainRoute.AppointmentsNavigator.route)
            }
            val viewModel = hiltViewModel<AppointmentsViewModel>(parentEntry)

            AppointmentDetailsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onNavigateToCancel = { navController.navigate(MainRoute.AppointmentCancel.route) },
                onNavigateToBookAgain = {
                    mainNavController.navigate(
                        "${MainRoute.Calendar.route}/${it.userId}/${it.slotDuration}/${it.productId}/${it.productName}"
                    )
                }
            )
        }

        composable(route = MainRoute.AppointmentCancel.route) { backStackEntry ->
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
                                //mainViewModel.decreaseAppointmentsNumber()
                                navController.popBackStack()
                            }
                            .onFailure { e ->
                                Timber.tag("Appointment").e("ERROR: onCreating Appointment $e")
                            }
                    }
                }
            )
        }
    }
}
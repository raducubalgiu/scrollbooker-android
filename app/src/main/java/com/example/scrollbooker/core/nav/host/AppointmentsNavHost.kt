package com.example.scrollbooker.core.nav.host

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.core.nav.transitions.slideEnterTransition
import com.example.scrollbooker.core.nav.transitions.slideExitTransition
import com.example.scrollbooker.screens.appointments.AppointmentDetailsScreen
import com.example.scrollbooker.screens.appointments.AppointmentsScreen
import com.example.scrollbooker.screens.appointments.AppointmentsViewModel

@Composable
fun AppointmentsNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = MainRoute.Appointments.route
    ) {
        composable(MainRoute.Appointments.route) { backStackEntry ->
            val viewModel = hiltViewModel<AppointmentsViewModel>(backStackEntry)
            AppointmentsScreen(viewModel)
        }

        composable(MainRoute.AppointmentDetails.route,
            enterTransition = slideEnterTransition(),
            popExitTransition = slideExitTransition()
        ) { backStackEntry ->
            val viewModel = hiltViewModel<AppointmentsViewModel>(backStackEntry)

            AppointmentDetailsScreen(viewModel)
        }
    }
}
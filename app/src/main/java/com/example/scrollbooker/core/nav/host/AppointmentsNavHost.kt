package com.example.scrollbooker.core.nav.host

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.appointments.presentation.AppointmentDetailsScreen
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsScreen
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsViewModel

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

        composable(MainRoute.AppointmentDetails.route) { backStackEntry ->
            val viewModel = hiltViewModel<AppointmentsViewModel>(backStackEntry)

            AppointmentDetailsScreen(viewModel)
        }
    }
}
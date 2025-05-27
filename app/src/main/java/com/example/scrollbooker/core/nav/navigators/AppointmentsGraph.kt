package com.example.scrollbooker.core.nav.navigators

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.scrollbooker.core.nav.routes.MainRoute
import com.example.scrollbooker.feature.appointments.presentation.AppointmentDetailsScreen
import com.example.scrollbooker.feature.appointments.presentation.AppointmentsScreen

fun NavGraphBuilder.appointmentsGraph(navController: NavController) {
    navigation(
        route = MainRoute.AppointmentsNavigator.route,
        startDestination = MainRoute.Appointments.route
    ) {
        composable(MainRoute.Appointments.route) {
            Box(Modifier.fillMaxSize().statusBarsPadding()) {
                AppointmentsScreen(navController)
            }
        }

        composable(MainRoute.AppointmentDetails.route) {
            AppointmentDetailsScreen(navController)
        }
    }
}
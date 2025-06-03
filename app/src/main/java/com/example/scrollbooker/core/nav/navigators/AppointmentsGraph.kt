package com.example.scrollbooker.core.nav.navigators

import androidx.compose.foundation.background
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
import com.example.scrollbooker.ui.theme.Background

//fun NavGraphBuilder.appointmentsGraph(navController: NavController) {
//    navigation(
//        route = MainRoute.AppointmentsNavigator.route,
//        startDestination = MainRoute.Appointments.route
//    ) {
//        composable(MainRoute.Appointments.route) {
//            AppointmentsScreen(navController)
//        }
//
//        composable(MainRoute.AppointmentDetails.route) {
//            AppointmentDetailsScreen(navController)
//        }
//    }
//}
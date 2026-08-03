package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.navigation.routes.MainRoute

class AppointmentsNavigator (
    private val navController: NavHostController
) {
    fun back() {
        navController.popBackStack()
    }

    fun toAppointmentDetails(appointmentId: Int) {
        navController.navigate("${MainRoute.AppointmentDetails.route}/$appointmentId") {
            launchSingleTop = true
        }
    }

    fun toCamera() {
        navController.navigate(MainRoute.Camera.route) {
            launchSingleTop = true
        }
    }
}
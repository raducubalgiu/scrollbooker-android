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

    fun toSocial(socialParam: SocialParam) {
        navController.navigate(MainRoute.Social.createRoute(socialParam)) {
            launchSingleTop = true
        }
    }

    fun toCamera(cameraParams: CameraParams) {
        navController.navigate(MainRoute.CameraNavigator.createRoute(cameraParams)) {
            launchSingleTop = true
        }
    }
}
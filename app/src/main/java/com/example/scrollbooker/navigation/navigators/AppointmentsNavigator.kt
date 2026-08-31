package com.example.scrollbooker.navigation.navigators

import androidx.navigation.NavHostController
import com.example.scrollbooker.core.enums.BookingSourceEnum
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
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

    fun toBookAgain(appointment: Appointment) {
        navController.navigateToBookingFromAppointment(
            appointment = appointment,
            source = BookingSourceEnum.BOOK_AGAIN
        )
    }

    fun toCamera(cameraParams: CameraParams) {
        navController.navigate(MainRoute.CameraNavigator.createRoute(cameraParams)) {
            launchSingleTop = true
        }
    }
}
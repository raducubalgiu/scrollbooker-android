package com.example.scrollbooker.ui.appointments.sheets

import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentUser

sealed class AppointmentSheetsContent {
    data class ReviewAppointmentSheet(
        val reviewUpdate: RatingReviewUpdate,
        val user: AppointmentUser
    ): AppointmentSheetsContent()
    data class CancelAppointmentSheet(val appointment: Appointment): AppointmentSheetsContent()
    object None: AppointmentSheetsContent()
}
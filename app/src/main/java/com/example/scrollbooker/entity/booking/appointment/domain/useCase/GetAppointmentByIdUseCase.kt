package com.example.scrollbooker.entity.booking.appointment.domain.useCase

import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository

class GetAppointmentByIdUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointmentId: Int): Appointment {
        return repository.getAppointmentById(appointmentId)
    }
}
package com.example.scrollbooker.entity.booking.appointment.domain.useCase

import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class CreateOwnClientAppointmentUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointmentCreate: AppointmentOwnClientCreate): Result<Unit> = runCatching {
        repository.createOwnClientAppointment(appointmentCreate)
    }
}
package com.example.scrollbooker.entity.booking.appointment.domain.useCase
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentCreate
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class CreateAppointmentUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointmentCreate: AppointmentCreate): Result<Unit> = runCatching {
        repository.createAppointment(appointmentCreate)
    }
}
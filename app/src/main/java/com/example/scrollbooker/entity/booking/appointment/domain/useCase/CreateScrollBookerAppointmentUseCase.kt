package com.example.scrollbooker.entity.booking.appointment.domain.useCase
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentScrollBookerCreate
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class CreateScrollBookerAppointmentUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointmentCreate: AppointmentScrollBookerCreate): Result<Unit> = runCatching {
        repository.createScrollBookerAppointment(appointmentCreate)
    }
}
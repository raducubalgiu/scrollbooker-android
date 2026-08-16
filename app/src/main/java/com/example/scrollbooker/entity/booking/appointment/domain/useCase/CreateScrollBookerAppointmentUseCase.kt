package com.example.scrollbooker.entity.booking.appointment.domain.useCase
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentScrollBookerCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class CreateScrollBookerAppointmentUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(
        appointmentCreate: AppointmentScrollBookerCreateDto
    ): Result<Appointment> = runCatching {
        repository.createScrollBookerAppointment(appointmentCreate)
    }
}
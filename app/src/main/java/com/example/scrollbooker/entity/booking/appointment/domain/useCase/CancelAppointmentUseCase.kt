package com.example.scrollbooker.entity.booking.appointment.domain.useCase
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class CancelAppointmentUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(
        appointmentId: Int,
        canceledReason: String,
        canceledByUserId: Int
    ): Result<Unit> = runCatching {
        repository.cancelAppointment(appointmentId, canceledReason, canceledByUserId)
    }
}
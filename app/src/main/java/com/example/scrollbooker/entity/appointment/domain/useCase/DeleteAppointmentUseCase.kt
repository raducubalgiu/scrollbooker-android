package com.example.scrollbooker.entity.appointment.domain.useCase
import com.example.scrollbooker.entity.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class DeleteAppointmentUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointmentId: Int, message: String): Result<Unit> = runCatching {
        repository.cancelAppointment(appointmentId, message)
    }
}
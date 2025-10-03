package com.example.scrollbooker.entity.booking.appointment.domain.useCase

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockRequest
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class BlockAppointmentsUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(request: List<AppointmentBlockRequest>): Result<Unit> = runCatching {
        repository.blockAppointments(request)
    }
}
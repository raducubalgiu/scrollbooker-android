package com.example.scrollbooker.entity.booking.appointment.domain.useCase

import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentLastMinuteRequest
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class CreateLastMinuteAppointmentUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(request: AppointmentLastMinuteRequest): Result<Unit> = runCatching {
        repository.createLastMinuteAppointment(request)
    }
}
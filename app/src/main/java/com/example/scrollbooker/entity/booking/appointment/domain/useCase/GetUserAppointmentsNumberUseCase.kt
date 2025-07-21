package com.example.scrollbooker.entity.booking.appointment.domain.useCase

import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository

class GetUserAppointmentsNumberUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getUserAppointmentsNumber()
    }
}
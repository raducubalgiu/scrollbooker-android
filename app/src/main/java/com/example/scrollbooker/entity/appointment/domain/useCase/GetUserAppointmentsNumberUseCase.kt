package com.example.scrollbooker.entity.appointment.domain.useCase
import com.example.scrollbooker.entity.appointment.domain.repository.AppointmentRepository

class GetUserAppointmentsNumberUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(): Int {
        return repository.getUserAppointmentsNumber()
    }
}
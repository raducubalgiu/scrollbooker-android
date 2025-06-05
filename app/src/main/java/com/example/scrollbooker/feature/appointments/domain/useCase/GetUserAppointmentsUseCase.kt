package com.example.scrollbooker.feature.appointments.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.feature.appointments.domain.model.Appointment
import com.example.scrollbooker.feature.appointments.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow

class GetUserAppointmentsUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(userId: Int, asCustomer: Boolean): Flow<PagingData<Appointment>> {
        return repository.getUserAppointments(userId, asCustomer)
    }
}
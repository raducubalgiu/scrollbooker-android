package com.example.scrollbooker.entity.booking.appointment.domain.useCase

import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow

class GetUserAppointmentsUseCase(
    private val repository: AppointmentRepository
) {
    operator fun invoke(asCustomer: Boolean?): Flow<PagingData<Appointment>> {
        return repository.getUserAppointments(asCustomer)
    }
}
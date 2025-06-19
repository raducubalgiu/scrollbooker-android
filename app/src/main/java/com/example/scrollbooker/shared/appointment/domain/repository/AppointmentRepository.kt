package com.example.scrollbooker.shared.appointment.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.shared.appointment.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getUserAppointments(asCustomer: Boolean): Flow<PagingData<Appointment>>
}
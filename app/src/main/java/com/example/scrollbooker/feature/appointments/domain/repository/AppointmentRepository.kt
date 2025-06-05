package com.example.scrollbooker.feature.appointments.domain.repository

import androidx.paging.PagingData
import com.example.scrollbooker.feature.appointments.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getUserAppointments(asCustomer: Boolean): Flow<PagingData<Appointment>>
}
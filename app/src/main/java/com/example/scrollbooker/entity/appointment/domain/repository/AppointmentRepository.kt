package com.example.scrollbooker.entity.appointment.domain.repository
import androidx.paging.PagingData
import com.example.scrollbooker.entity.appointment.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getUserAppointments(asCustomer: Boolean?): Flow<PagingData<Appointment>>
    suspend fun getUserAppointmentsNumber(): Int
    suspend fun cancelAppointment(appointmentId: Int, message: String)
}
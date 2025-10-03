package com.example.scrollbooker.entity.booking.appointment.domain.repository
import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockRequest
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentCreate
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getUserAppointments(asCustomer: Boolean?): Flow<PagingData<Appointment>>
    suspend fun createAppointment(appointmentCreate: AppointmentCreate)
    suspend fun getUserAppointmentsNumber(): Int
    suspend fun cancelAppointment(appointmentId: Int, message: String)
    suspend fun blockAppointments(request: List<AppointmentBlockRequest>)
}
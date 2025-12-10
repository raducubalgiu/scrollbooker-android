package com.example.scrollbooker.entity.booking.appointment.domain.repository
import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockRequest
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentScrollBookerCreate
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getUserAppointments(asCustomer: Boolean?): Flow<PagingData<Appointment>>
    suspend fun createScrollBookerAppointment(appointmentCreate: AppointmentScrollBookerCreate)
    suspend fun createOwnClientAppointment(appointmentCreate: AppointmentOwnClientCreate)
    suspend fun blockAppointments(request: AppointmentBlockRequest)
    suspend fun getUserAppointmentsNumber(): Int
    suspend fun cancelAppointment(appointmentId: Int, message: String)
}
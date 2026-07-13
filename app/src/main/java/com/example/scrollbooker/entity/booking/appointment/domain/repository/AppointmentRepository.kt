package com.example.scrollbooker.entity.booking.appointment.domain.repository
import androidx.paging.PagingData
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentBlockRequest
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentLastMinuteRequest
import com.example.scrollbooker.entity.booking.appointment.data.remote.AppointmentScrollBookerCreateDto
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.model.AppointmentOwnClientCreate
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getUserAppointments(asCustomer: Boolean?): Flow<PagingData<Appointment>>
    suspend fun getUserAppointmentsNumber(): Int
    suspend fun getAppointmentById(appointmentId: Int): Appointment
    suspend fun createScrollBookerAppointment(appointmentCreate: AppointmentScrollBookerCreateDto)
    suspend fun createOwnClientAppointment(appointmentCreate: AppointmentOwnClientCreate)
    suspend fun createLastMinuteAppointment(lastMinuteRequest: AppointmentLastMinuteRequest)
    suspend fun blockAppointments(request: AppointmentBlockRequest)
    suspend fun cancelAppointment(appointmentId: Int, canceledReason: String, canceledByUserId: Int)
}
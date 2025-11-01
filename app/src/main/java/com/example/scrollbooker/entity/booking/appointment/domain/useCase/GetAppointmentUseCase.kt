package com.example.scrollbooker.entity.booking.appointment.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import timber.log.Timber

class GetAppointmentUseCase(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(appointmentId: Int): FeatureState<Appointment> {
        return try {
            val appointment = repository.getAppointment(appointmentId)
            FeatureState.Success(appointment)
        } catch (e: Exception) {
            Timber.tag("Appointments").e("ERROR: on Fetching Appointment by id: $e")
            FeatureState.Error(e)
        }
    }
}
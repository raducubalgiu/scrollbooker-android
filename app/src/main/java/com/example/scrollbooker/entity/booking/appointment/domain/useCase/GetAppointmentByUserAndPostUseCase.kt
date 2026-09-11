package com.example.scrollbooker.entity.booking.appointment.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.appointment.domain.model.Appointment
import com.example.scrollbooker.entity.booking.appointment.domain.repository.AppointmentRepository
import javax.inject.Inject

class GetAppointmentByUserAndPostUseCase @Inject constructor(
    private val repository: AppointmentRepository
) {
    suspend operator fun invoke(
        userId: Int,
        postId: Int,
    ): Result<Appointment> {
        return runSuspendCatching {
            repository.getAppointmentByUserAndPost(userId, postId)
        }
    }
}
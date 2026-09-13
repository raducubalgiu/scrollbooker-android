package com.example.scrollbooker.entity.booking.userCalendarSettings.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.model.UserCalendarSettings
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.repository.UserCalendarSettingsRepository
import javax.inject.Inject

class UpdateAppointmentGapUseCase @Inject constructor(
    private val repository: UserCalendarSettingsRepository
) {
    suspend operator fun invoke(appointmentGapMinutes: Int): Result<UserCalendarSettings> = runSuspendCatching {
        repository.updateAppointmentGap(appointmentGapMinutes)
    }
}

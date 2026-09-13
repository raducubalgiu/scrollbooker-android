package com.example.scrollbooker.entity.booking.userCalendarSettings.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.model.UserCalendarSettings
import com.example.scrollbooker.entity.booking.userCalendarSettings.domain.repository.UserCalendarSettingsRepository
import javax.inject.Inject

class UpdateSlotDurationUseCase @Inject constructor(
    private val repository: UserCalendarSettingsRepository
) {
    suspend operator fun invoke(slotDurationMinutes: Int): Result<UserCalendarSettings> = runSuspendCatching {
        repository.updateSlotDuration(slotDurationMinutes)
    }
}

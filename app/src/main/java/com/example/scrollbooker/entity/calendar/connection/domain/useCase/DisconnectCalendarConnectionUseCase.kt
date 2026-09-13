package com.example.scrollbooker.entity.calendar.connection.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.calendar.connection.domain.repository.CalendarConnectionRepository
import javax.inject.Inject

class DisconnectCalendarConnectionUseCase @Inject constructor(
    private val repository: CalendarConnectionRepository
) {
    suspend operator fun invoke(): Result<Unit> = runSuspendCatching {
        repository.disconnectCalendarConnection()
    }
}

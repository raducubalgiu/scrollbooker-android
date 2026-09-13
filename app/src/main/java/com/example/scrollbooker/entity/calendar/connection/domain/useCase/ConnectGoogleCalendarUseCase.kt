package com.example.scrollbooker.entity.calendar.connection.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.calendar.connection.domain.model.CalendarConnection
import com.example.scrollbooker.entity.calendar.connection.domain.repository.CalendarConnectionRepository
import javax.inject.Inject

class ConnectGoogleCalendarUseCase @Inject constructor(
    private val repository: CalendarConnectionRepository
) {
    suspend operator fun invoke(serverAuthCode: String): Result<CalendarConnection> =
        runSuspendCatching {
            repository.connectGoogleCalendar(serverAuthCode = serverAuthCode)
        }
}

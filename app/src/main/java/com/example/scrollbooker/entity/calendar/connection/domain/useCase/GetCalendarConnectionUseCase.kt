package com.example.scrollbooker.entity.calendar.connection.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.calendar.connection.domain.model.CalendarConnection
import com.example.scrollbooker.entity.calendar.connection.domain.repository.CalendarConnectionRepository
import javax.inject.Inject

class GetCalendarConnectionUseCase @Inject constructor(
    private val repository: CalendarConnectionRepository
) {
    suspend operator fun invoke(businessId: Int): Result<CalendarConnection?> = runSuspendCatching {
        repository.getCalendarConnection(businessId)
    }
}

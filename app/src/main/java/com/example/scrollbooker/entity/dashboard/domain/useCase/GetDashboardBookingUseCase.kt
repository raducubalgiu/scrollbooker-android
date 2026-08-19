package com.example.scrollbooker.entity.dashboard.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBooking
import com.example.scrollbooker.entity.dashboard.domain.repository.DashboardRepository

class GetDashboardBookingUseCase(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(startDate: String, endDate: String): Result<DashboardBooking> {
        return runSuspendCatching {
            repository.getDashboardBooking(startDate, endDate)
        }
    }
}
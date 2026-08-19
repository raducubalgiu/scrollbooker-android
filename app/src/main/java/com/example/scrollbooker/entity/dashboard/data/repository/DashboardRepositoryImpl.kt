package com.example.scrollbooker.entity.dashboard.data.repository
import com.example.scrollbooker.entity.dashboard.data.mapper.toDomain
import com.example.scrollbooker.entity.dashboard.data.remote.DashboardApiService
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBooking
import com.example.scrollbooker.entity.dashboard.domain.repository.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val apiService: DashboardApiService
): DashboardRepository {
    override suspend fun getDashboardBooking(
        startDate: String,
        endDate: String
    ): DashboardBooking {
           return apiService.getDashboardBooking(startDate, endDate).toDomain()
    }
}
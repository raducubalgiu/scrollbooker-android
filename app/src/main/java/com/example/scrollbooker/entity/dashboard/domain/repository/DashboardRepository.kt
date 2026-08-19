package com.example.scrollbooker.entity.dashboard.domain.repository
import com.example.scrollbooker.entity.dashboard.domain.model.DashboardBooking

interface DashboardRepository {
    suspend fun getDashboardBooking(startDate: String, endDate: String): DashboardBooking
}
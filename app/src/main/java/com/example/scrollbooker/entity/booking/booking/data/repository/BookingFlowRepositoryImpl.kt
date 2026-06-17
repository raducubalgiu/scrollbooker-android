package com.example.scrollbooker.entity.booking.booking.data.repository

import com.example.scrollbooker.entity.booking.booking.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.booking.data.remote.BookingFlowApiService
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow
import com.example.scrollbooker.entity.booking.booking.domain.repository.BookingFlowRepository
import javax.inject.Inject

class BookingFlowRepositoryImpl @Inject constructor(
    private val apiService: BookingFlowApiService
): BookingFlowRepository {
    override suspend fun getBookingFlow(businessId: Int, employeeId: Int?): BookingFlow {
        return apiService.getBookingFlow(businessId, employeeId).toDomain()
    }
}
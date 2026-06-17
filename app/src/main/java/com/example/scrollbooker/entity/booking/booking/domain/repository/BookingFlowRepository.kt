package com.example.scrollbooker.entity.booking.booking.domain.repository

import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow

interface BookingFlowRepository {
    suspend fun getBookingFlow(businessId: Int, employeeId: Int?): BookingFlow
}
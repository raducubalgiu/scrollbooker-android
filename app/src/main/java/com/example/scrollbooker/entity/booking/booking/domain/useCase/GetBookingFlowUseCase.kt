package com.example.scrollbooker.entity.booking.booking.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow
import com.example.scrollbooker.entity.booking.booking.domain.repository.BookingFlowRepository
import timber.log.Timber
import javax.inject.Inject

class GetBookingFlowUseCase @Inject constructor(
    private val repository: BookingFlowRepository
) {
    suspend operator fun invoke(businessId: Int, employeeId: Int?): FeatureState<BookingFlow> {
        try {
            val response = repository.getBookingFlow(businessId, employeeId)
            return FeatureState.Success(response)

        } catch (e: Exception) {
            Timber.tag("Booking Flow").e("ERROR: on Fetching Booking Flow $e")
            return FeatureState.Error(e)
        }
    }
}
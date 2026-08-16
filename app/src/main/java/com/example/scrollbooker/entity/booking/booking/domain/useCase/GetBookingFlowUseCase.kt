package com.example.scrollbooker.entity.booking.booking.domain.useCase
import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.booking.domain.model.BookingFlow
import com.example.scrollbooker.entity.booking.booking.domain.repository.BookingFlowRepository
import javax.inject.Inject

class GetBookingFlowUseCase @Inject constructor(
    private val repository: BookingFlowRepository
) {
    suspend operator fun invoke(businessId: Int, employeeId: Int?): Result<BookingFlow> {
        return runSuspendCatching {
            repository.getBookingFlow(businessId, employeeId)
        }
    }
}

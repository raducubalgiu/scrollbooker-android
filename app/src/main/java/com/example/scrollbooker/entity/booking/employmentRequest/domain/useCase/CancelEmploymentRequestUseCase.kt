package com.example.scrollbooker.entity.booking.employmentRequest.domain.useCase

import com.example.scrollbooker.entity.booking.employmentRequest.domain.repository.EmploymentRequestRepository
import javax.inject.Inject

class CancelEmploymentRequestUseCase @Inject constructor(
    private val repository: EmploymentRequestRepository
) {
    suspend operator fun invoke(employmentId: Int): Result<Unit> = runCatching {
        repository.cancelEmploymentRequest(employmentId)
    }
}
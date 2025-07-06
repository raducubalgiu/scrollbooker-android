package com.example.scrollbooker.entity.employmentRequest.domain.useCase

import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.entity.employmentRequest.domain.repository.EmploymentRequestRepository
import javax.inject.Inject

class RespondEmploymentRequestUseCase @Inject constructor(
    private val repository: EmploymentRequestRepository
) {
    suspend operator fun invoke(status: EmploymentRequestStatusEnum, employmentId: Int): Result<Unit> = runCatching {
        repository.responseEmploymentRequest(status, employmentId)
    }
}
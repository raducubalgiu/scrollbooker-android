package com.example.scrollbooker.entity.booking.employmentRequest.domain.useCase
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequestCreate
import com.example.scrollbooker.entity.booking.employmentRequest.domain.repository.EmploymentRequestRepository
import javax.inject.Inject

class CreateEmploymentRequestUseCase @Inject constructor(
    private val repository: EmploymentRequestRepository
) {
    suspend operator fun invoke(dto: EmploymentRequestCreate): Result<Unit> = runCatching {
        repository.createEmploymentRequest(dto)
    }
}
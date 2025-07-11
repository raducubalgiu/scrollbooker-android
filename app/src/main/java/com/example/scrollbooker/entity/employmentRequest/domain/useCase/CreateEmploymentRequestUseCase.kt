package com.example.scrollbooker.entity.employmentRequest.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.entity.employmentRequest.domain.repository.EmploymentRequestRepository
import timber.log.Timber
import javax.inject.Inject

class CreateEmploymentRequestUseCase @Inject constructor(
    private val repository: EmploymentRequestRepository
) {
    suspend operator fun invoke(dto: EmploymentRequestCreateDto): FeatureState<Unit> {
        return try {
            repository.createEmploymentRequest(dto)
            FeatureState.Success(Unit)
        } catch(e: Exception) {
            Timber.tag("Employment Request").e("ERROR: on creating Employment Request $e")
            FeatureState.Error(e)
        }
    }
}
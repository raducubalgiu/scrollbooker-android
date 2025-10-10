package com.example.scrollbooker.entity.booking.employmentRequest.domain.useCase

import com.example.scrollbooker.core.util.FeatureState
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.booking.employmentRequest.domain.repository.EmploymentRequestRepository
import timber.log.Timber
import javax.inject.Inject

class GetEmploymentRequestByIdUseCase @Inject constructor(
    private val repository: EmploymentRequestRepository
) {
    suspend operator fun invoke(employmentId: Int): FeatureState<EmploymentRequest> {
        return try {
            val response = repository.getEmploymentRequestById(employmentId)
            return FeatureState.Success(response)
        } catch(e: Exception) {
            Timber.tag("Employment Requests").e("ERROR: on Fetching Employment Request $e")
            return FeatureState.Error(e)
        }
    }
}
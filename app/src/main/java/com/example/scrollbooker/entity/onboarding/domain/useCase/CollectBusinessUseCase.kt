package com.example.scrollbooker.entity.onboarding.domain.useCase
import com.example.scrollbooker.entity.onboarding.data.remote.BusinessCreateRequest
import com.example.scrollbooker.entity.onboarding.domain.model.BusinessCreateResponse
import com.example.scrollbooker.entity.onboarding.domain.repository.OnboardingRepository
import javax.inject.Inject

class CollectBusinessUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(
        description: String?,
        placeId: String,
        businessTypeId: Int,
        ownerFullName: String
    ): Result<BusinessCreateResponse> = runCatching {
        val request = BusinessCreateRequest(
            description = description,
            placeId = placeId,
            businessTypeId = businessTypeId,
            ownerFullName = ownerFullName
        )
        repository.collectBusiness(request)
    }
}
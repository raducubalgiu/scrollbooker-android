package com.example.scrollbooker.entity.business.domain.useCase
import com.example.scrollbooker.entity.business.domain.model.BusinessCreateResponse
import com.example.scrollbooker.entity.business.domain.repository.BusinessRepository
import javax.inject.Inject

class CreateBusinessUseCase @Inject constructor(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(
        description: String?,
        placeId: String,
        businessTypeId: Int,
        ownerFullName: String
    ): Result<BusinessCreateResponse> = runCatching {
        repository.createBusiness(
            description = description,
            placeId = placeId,
            businessTypeId = businessTypeId,
            ownerFullName = ownerFullName
        )
    }
}
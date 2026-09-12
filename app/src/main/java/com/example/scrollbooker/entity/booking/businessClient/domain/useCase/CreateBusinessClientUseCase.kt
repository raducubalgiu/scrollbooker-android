package com.example.scrollbooker.entity.booking.businessClient.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClientCreate
import com.example.scrollbooker.entity.booking.businessClient.domain.repository.BusinessClientRepository
import javax.inject.Inject

class CreateBusinessClientUseCase @Inject constructor(
    private val repository: BusinessClientRepository
) {
    suspend operator fun invoke(businessId: Int, request: BusinessClientCreate): Result<BusinessClient> {
        return runSuspendCatching {
            repository.createBusinessClient(businessId, request)
        }
    }
}

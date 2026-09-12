package com.example.scrollbooker.entity.booking.businessClient.domain.useCase

import com.example.scrollbooker.core.util.runSuspendCatching
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.entity.booking.businessClient.domain.repository.BusinessClientRepository
import javax.inject.Inject

class SearchBusinessClientsUseCase @Inject constructor(
    private val repository: BusinessClientRepository
) {
    suspend operator fun invoke(businessId: Int, query: String?): Result<List<BusinessClient>> {
        return runSuspendCatching {
            repository.getBusinessClients(businessId, query)
        }
    }
}

package com.example.scrollbooker.entity.booking.businessClient.data.repository

import com.example.scrollbooker.entity.booking.businessClient.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.businessClient.data.mappers.toDto
import com.example.scrollbooker.entity.booking.businessClient.data.remote.BusinessClientsApiService
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClientCreate
import com.example.scrollbooker.entity.booking.businessClient.domain.repository.BusinessClientRepository
import javax.inject.Inject

private const val SEARCH_PAGE_LIMIT = 30

class BusinessClientRepositoryImpl @Inject constructor(
    private val apiService: BusinessClientsApiService
): BusinessClientRepository {
    override suspend fun getBusinessClients(businessId: Int, query: String?): List<BusinessClient> {
        return apiService.getBusinessClients(
            businessId = businessId,
            query = query,
            page = 1,
            limit = SEARCH_PAGE_LIMIT
        ).results.map { it.toDomain() }
    }

    override suspend fun createBusinessClient(businessId: Int, request: BusinessClientCreate): BusinessClient {
        return apiService.createBusinessClient(
            businessId = businessId,
            request = request.toDto()
        ).toDomain()
    }
}

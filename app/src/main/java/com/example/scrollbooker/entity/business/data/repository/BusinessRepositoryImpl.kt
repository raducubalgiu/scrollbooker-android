package com.example.scrollbooker.entity.business.data.repository

import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.business.data.mappers.toDomain
import com.example.scrollbooker.entity.business.data.remote.BusinessApiService
import com.example.scrollbooker.entity.business.data.remote.BusinessServicesUpdateRequest
import com.example.scrollbooker.entity.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.business.domain.repository.BusinessRepository
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val apiService: BusinessApiService
): BusinessRepository {
    override suspend fun searchBusinessAddress(query: String): List<BusinessAddress> {
        return apiService.searchBusinessAddress(query).map { it.toDomain() }
    }

    override suspend fun updateBusinessServices(serviceIds: List<Int>): AuthState {
        val request = BusinessServicesUpdateRequest(serviceIds)

        return apiService.updateBusinessServices(request).toDomain()
    }
}
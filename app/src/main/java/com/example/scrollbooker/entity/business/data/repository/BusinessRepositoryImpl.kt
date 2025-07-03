package com.example.scrollbooker.entity.business.data.repository

import com.example.scrollbooker.entity.business.data.mappers.toDomain
import com.example.scrollbooker.entity.business.data.remote.BusinessApiService
import com.example.scrollbooker.entity.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.business.domain.repository.BusinessRepository
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val apiService: BusinessApiService
): BusinessRepository {
    override suspend fun searchBusinessAddress(query: String): List<BusinessAddress> {
        return apiService.searchBusinessAddress(query).map { it.toDomain() }
    }
}
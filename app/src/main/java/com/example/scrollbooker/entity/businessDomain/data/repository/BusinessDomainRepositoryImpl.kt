package com.example.scrollbooker.entity.businessDomain.data.repository

import com.example.scrollbooker.entity.businessDomain.data.mappers.toDomain
import com.example.scrollbooker.entity.businessDomain.data.remote.BusinessDomainApiService
import com.example.scrollbooker.entity.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.businessDomain.domain.repository.BusinessDomainRepository
import javax.inject.Inject

class BusinessDomainRepositoryImpl @Inject constructor(
    private val apiService: BusinessDomainApiService
): BusinessDomainRepository {
    override suspend fun getBusinessDomains(): List<BusinessDomain> {
        return apiService.getAllBusinessDomains().map { it.toDomain() }
    }
}
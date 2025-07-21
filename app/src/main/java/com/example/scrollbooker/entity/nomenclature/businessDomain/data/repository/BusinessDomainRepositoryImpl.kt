package com.example.scrollbooker.entity.nomenclature.businessDomain.data.repository

import com.example.scrollbooker.entity.nomenclature.businessDomain.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.data.remote.BusinessDomainApiService
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.repository.BusinessDomainRepository
import javax.inject.Inject

class BusinessDomainRepositoryImpl @Inject constructor(
    private val apiService: BusinessDomainApiService
): BusinessDomainRepository {
    override suspend fun getBusinessDomains(): List<BusinessDomain> {
        return apiService.getAllBusinessDomains().map { it.toDomain() }
    }
}
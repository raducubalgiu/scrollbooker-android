package com.example.scrollbooker.entity.businessDomain.domain.repository
import com.example.scrollbooker.entity.businessDomain.domain.model.BusinessDomain

interface BusinessDomainRepository {
    suspend fun getBusinessDomains(): List<BusinessDomain>
}
package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.repository
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain

interface BusinessDomainRepository {
    suspend fun getAllBusinessDomains(): List<BusinessDomain>
}
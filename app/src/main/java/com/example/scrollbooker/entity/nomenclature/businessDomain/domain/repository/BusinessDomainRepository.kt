package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.repository
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomainsWithBusinessTypes

interface BusinessDomainRepository {
    suspend fun getBusinessDomains(): List<BusinessDomain>
    suspend fun getAllBusinessDomainsWithBusinessTypes(): List<BusinessDomainsWithBusinessTypes>
}
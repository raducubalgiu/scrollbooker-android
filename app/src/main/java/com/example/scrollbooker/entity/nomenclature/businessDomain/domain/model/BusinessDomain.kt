package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model
import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain

data class BusinessDomain(
    val id: Int,
    val name: String,
    val shortName: String,
    val serviceDomains: List<ServiceDomain>,
    val businessTypes: List<BusinessType>
)
package com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model

import com.example.scrollbooker.entity.nomenclature.businessType.domain.model.BusinessType

data class BusinessDomainsWithBusinessTypes(
    val id: Int,
    val name: String,
    val shortName: String,
    val businessTypes: List<BusinessType>
)
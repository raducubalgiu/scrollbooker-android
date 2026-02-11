package com.example.scrollbooker.entity.nomenclature.businessDomain.data.mappers
import com.example.scrollbooker.entity.nomenclature.businessDomain.data.remote.BusinessDomainDto
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain
import com.example.scrollbooker.entity.nomenclature.businessType.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers.toDomain

fun BusinessDomainDto.toDomain(): BusinessDomain {
    return BusinessDomain(
        id = id,
        name = name,
        shortName = shortName,
        serviceDomains = serviceDomains.map { it.toDomain() },
        businessTypes = businessTypes.map { it.toDomain() }
    )
}
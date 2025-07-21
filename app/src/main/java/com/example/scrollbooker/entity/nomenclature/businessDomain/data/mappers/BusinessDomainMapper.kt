package com.example.scrollbooker.entity.nomenclature.businessDomain.data.mappers
import com.example.scrollbooker.entity.nomenclature.businessDomain.data.remote.BusinessDomainDto
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomain

fun BusinessDomainDto.toDomain(): BusinessDomain {
    return BusinessDomain(
        id = id,
        name = name,
        shortName = shortName
    )
}
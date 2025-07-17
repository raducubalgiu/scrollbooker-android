package com.example.scrollbooker.entity.businessDomain.data.mappers
import com.example.scrollbooker.entity.businessDomain.data.remote.BusinessDomainDto
import com.example.scrollbooker.entity.businessDomain.domain.model.BusinessDomain

fun BusinessDomainDto.toDomain(): BusinessDomain {
    return BusinessDomain(
        id = id,
        name = name,
        shortName = shortName
    )
}
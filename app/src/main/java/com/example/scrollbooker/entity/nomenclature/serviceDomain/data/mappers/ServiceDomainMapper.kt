package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers

import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomain

fun ServiceDomainDto.toDomain(): ServiceDomain {
    return ServiceDomain(
        id = id,
        name = name,
        description = description,
        url = url,
        thumbnailUrl = thumbnailUrl
    )
}
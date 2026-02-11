package com.example.scrollbooker.entity.nomenclature.service.data.mappers

import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceDto
import com.example.scrollbooker.entity.nomenclature.service.domain.model.Service

fun ServiceDto.toDomain(): Service {
    return Service(
        id = id,
        name = name,
        shortName = shortName,
        description = description,
        businessDomainId = businessDomainId
    )
}
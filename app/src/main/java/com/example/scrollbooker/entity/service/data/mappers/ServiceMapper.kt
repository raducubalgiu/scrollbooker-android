package com.example.scrollbooker.entity.service.data.mappers

import com.example.scrollbooker.entity.service.data.remote.ServiceDto
import com.example.scrollbooker.entity.service.domain.model.Service

fun ServiceDto.toDomain(): Service {
    return Service(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}
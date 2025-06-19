package com.example.scrollbooker.shared.service.data.mappers

import com.example.scrollbooker.shared.service.data.remote.ServiceDto
import com.example.scrollbooker.shared.service.domain.model.Service

fun ServiceDto.toDomain(): Service {
    return Service(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}
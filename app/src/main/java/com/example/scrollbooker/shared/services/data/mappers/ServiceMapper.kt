package com.example.scrollbooker.shared.services.data.mappers

import com.example.scrollbooker.shared.services.data.remote.ServiceDto
import com.example.scrollbooker.shared.services.domain.model.Service

fun ServiceDto.toDomain(): Service {
    return Service(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}
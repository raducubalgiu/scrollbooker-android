package com.example.scrollbooker.feature.myBusiness.services.data.mappers

import com.example.scrollbooker.feature.myBusiness.services.data.remote.ServiceDto
import com.example.scrollbooker.feature.myBusiness.services.domain.model.Service

fun ServiceDto.toDomain(): Service {
    return Service(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}
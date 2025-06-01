package com.example.scrollbooker.feature.myBusiness.data.mappers

import com.example.scrollbooker.feature.myBusiness.data.remote.service.ServiceDto
import com.example.scrollbooker.feature.myBusiness.domain.model.Service

fun ServiceDto.toDomain(): Service {
    return Service(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}
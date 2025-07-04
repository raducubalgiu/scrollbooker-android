package com.example.scrollbooker.entity.business.data.mappers

import com.example.scrollbooker.entity.business.data.remote.BusinessDto
import com.example.scrollbooker.entity.business.domain.model.Business

fun BusinessDto.toDomain(): Business {
    return Business(
        id = id,
        businessTypeId = businessTypeId,
        ownerId = ownerId,
        description = description,
        timezone = timezone,
        address = address,
        coordinates = coordinates,
        hasEmployees = hasEmployees
    )
}
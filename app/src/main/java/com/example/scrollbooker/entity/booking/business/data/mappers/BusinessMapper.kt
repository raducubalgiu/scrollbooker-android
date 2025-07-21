package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.appointment.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessDto
import com.example.scrollbooker.entity.booking.business.domain.model.Business

fun BusinessDto.toDomain(): Business {
    return Business(
        id = id,
        businessTypeId = businessTypeId,
        ownerId = ownerId,
        description = description,
        timezone = timezone,
        address = address,
        coordinates = coordinates.toDomain(),
        hasEmployees = hasEmployees
    )
}
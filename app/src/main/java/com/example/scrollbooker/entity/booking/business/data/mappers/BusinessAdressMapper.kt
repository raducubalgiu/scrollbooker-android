package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessAddressDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessAddress

fun BusinessAddressDto.toDomain(): BusinessAddress {
    return BusinessAddress(
        description = description,
        placeId = placeId
    )
}
package com.example.scrollbooker.entity.business.data.mappers

import com.example.scrollbooker.entity.business.data.remote.BusinessAddressDto
import com.example.scrollbooker.entity.business.domain.model.BusinessAddress

fun BusinessAddressDto.toDomain(): BusinessAddress {
    return BusinessAddress(
        description = description,
        placeId = placeId
    )
}
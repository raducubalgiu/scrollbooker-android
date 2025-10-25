package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessLocationDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation

fun BusinessLocationDto.toDomain(): BusinessLocation {
    return BusinessLocation(
        distance = distance,
        address = address,
        mapUrl = mapUrl
    )
}
package com.example.scrollbooker.entity.booking.businessClient.data.mappers

import com.example.scrollbooker.entity.booking.businessClient.data.remote.BusinessClientCreateDto
import com.example.scrollbooker.entity.booking.businessClient.data.remote.BusinessClientDto
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClientCreate

fun BusinessClientDto.toDomain(): BusinessClient {
    return BusinessClient(
        id = id,
        businessId = businessId,
        userId = userId,
        fullname = fullname,
        phone = phone,
    )
}

fun BusinessClientCreate.toDto(): BusinessClientCreateDto {
    return BusinessClientCreateDto(
        fullname = fullname,
        phone = phone,
    )
}

package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessOwnerDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessOwner

fun BusinessOwnerDto.toDomain(): BusinessOwner {
    return BusinessOwner(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        ratingsAverage = ratingsAverage,
        ratingsCount = ratingsCount,
        profession = profession
    )
}
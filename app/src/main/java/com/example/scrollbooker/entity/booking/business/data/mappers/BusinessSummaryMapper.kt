package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessSummaryDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSummary

fun BusinessSummaryDto.toDomain(): BusinessSummary {
    return BusinessSummary(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        ratingsAverage = ratingsAverage,
        ratingsCount = ratingsCount,
        profession = profession
    )
}
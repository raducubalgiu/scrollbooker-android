package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessSheetDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet

fun BusinessSheetDto.toDomain(): BusinessSheet {
    return BusinessSheet(
        business = business.toDomain(),
        businessShortDomain = businessShortDomain,
        address = address,
        coordinates = coordinates,
        hasVideo = hasVideo,
        mediaPreview = mediaPreview?.toDomain(),
    )
}
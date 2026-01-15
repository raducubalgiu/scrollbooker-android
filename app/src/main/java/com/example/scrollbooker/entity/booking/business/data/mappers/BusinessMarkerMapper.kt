package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.core.enums.BusinessShortDomainEnum
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMarkerDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker

fun BusinessMarkerDto.toDomain(): BusinessMarker {
    return BusinessMarker(
        id = id,
        owner = owner.toDomain(),
        businessShortDomain = BusinessShortDomainEnum.fromKeyOrUnknown(businessShortDomain),
        address = address,
        coordinates = coordinates,
        hasVideo = hasVideo,
        mediaPreview = mediaPreview?.toDomain(),
        isPrimary = isPrimary
    )
}
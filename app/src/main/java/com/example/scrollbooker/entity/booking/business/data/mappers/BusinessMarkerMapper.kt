package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMarkerDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMediaPreviewDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaPreview

fun BusinessMarkerDto.toDomain(): BusinessMarker {
    return BusinessMarker(
        businessId = businessId,
        businessName = businessName,
        businessShortDomain = businessShortDomain,
        address = address,
        coordinates = coordinates,
        ratingsAverage = ratingsAverage,
        ratingsCount = ratingsCount,
        hasVideo = hasVideo,
        mediaPreview = mediaPreview?.toDomain(),
        isPrimary = isPrimary
    )
}

fun BusinessMediaPreviewDto.toDomain(): BusinessMediaPreview {
    return BusinessMediaPreview(
        type = type,
        thumbnailUrl = thumbnailUrl,
        previewVideoUrl = previewVideoUrl
    )
}
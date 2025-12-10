package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMarkerDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMediaPreviewDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMarker
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaPreview

fun BusinessMarkerDto.toDomain(): BusinessMarker {
    return BusinessMarker(
        owner = owner.toDomain(),
        businessShortDomain = businessShortDomain,
        address = address,
        coordinates = coordinates,
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
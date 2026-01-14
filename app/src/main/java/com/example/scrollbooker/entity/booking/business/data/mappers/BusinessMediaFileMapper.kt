package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMediaFileDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaFile

fun BusinessMediaFileDto.toDomain(): BusinessMediaFile {
    return BusinessMediaFile(
        url = url,
        urlKey = urlKey,
        thumbnailUrl = thumbnailUrl,
        thumbnailKey = thumbnailKey,
        type = type,
        orderIndex = orderIndex
    )
}
package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessSheetDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessSheet
import com.example.scrollbooker.entity.booking.products.data.mappers.toDomain

fun BusinessSheetDto.toDomain(): BusinessSheet {
    return BusinessSheet(
        owner = owner.toDomain(),
        businessShortDomain = businessShortDomain,
        address = address,
        coordinates = coordinates,
        hasVideo = hasVideo,
        mediaFiles = mediaFiles.map { it.toDomain() },
        products = products.map { it.toDomain() }
    )
}
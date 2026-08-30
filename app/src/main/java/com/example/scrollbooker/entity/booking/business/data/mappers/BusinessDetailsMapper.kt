package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessDetailsDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessDetails
import com.example.scrollbooker.entity.booking.schedule.data.mappers.toDomain

fun BusinessDetailsDto.toDomain(): BusinessDetails {
    return BusinessDetails(
        id = id,
        owner = owner.toDomain(),
        location = location.toDomain(),
        mediaFiles = mediaFiles?.map { it.toDomain() } ?: emptyList(),
        hasEmployees = hasEmployees,
        schedules = schedules.map { it.toDomain() }
    )
}
package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.appointment.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessDto
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.schedule.data.mappers.toDomain
import com.example.scrollbooker.entity.nomenclature.service.data.mappers.toDomain

fun BusinessDto.toDomain(): Business {
    return Business(
        id = id,
        businessTypeId = businessTypeId,
        ownerId = ownerId,
        description = description,
        timezone = timezone,
        address = address,
        coordinates = coordinates.toDomain(),
        hasEmployees = hasEmployees,
        formattedAddress = formattedAddress,
        city = city,
        countryCode = countryCode,
        mapUrl = mapUrl,
        services = services.map { it.toDomain() },
        schedules = schedules.map { it.toDomain() }
    )
}
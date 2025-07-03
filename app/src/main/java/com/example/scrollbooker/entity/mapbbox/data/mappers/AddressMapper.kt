package com.example.scrollbooker.entity.mapbbox.data.mappers

import com.example.scrollbooker.entity.mapbbox.data.remote.AddressDto
import com.example.scrollbooker.entity.mapbbox.domain.model.Address

fun AddressDto.toDomain(): Address {
    val lat = geometry.coordinates.getOrNull(1) ?: 0.0
    val lng = geometry.coordinates.getOrNull(0) ?: 0.0

    val city = context.find { it.id.startsWith("place") }?.text.orEmpty()
    val region = context.find { it.id.startsWith("region") }?.text.orEmpty()
    val country = context.find { it.id.startsWith("country") }?.text.orEmpty()
    val postalCode = context.find { it.id.startsWith("postcode") }?.text.orEmpty()

    return Address(
        fullAddress = placeName,
        street = text,
        city = city,
        region = region,
        country = country,
        postalCode = postalCode,
        lat = lat,
        lng = lng
    )
}
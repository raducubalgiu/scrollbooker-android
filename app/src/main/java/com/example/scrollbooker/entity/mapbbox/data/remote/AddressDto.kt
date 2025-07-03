package com.example.scrollbooker.entity.mapbbox.data.remote

import com.google.gson.annotations.SerializedName

data class AddressDto(
    @SerializedName("place_name")
    val placeName: String,

    val text: String,
    val geometry: GeometryDto,
    val context: List<ContextItemDto> = emptyList()
)

data class GeometryDto(
    val coordinates: List<Double>
)

data class ContextItemDto(
    val id: String,
    val text: String
)

data class AddressResponseDto(
    val features: List<AddressDto>
)
package com.example.scrollbooker.entity.booking.products.data.remote

import com.google.gson.annotations.SerializedName

data class LinkedProductsBusinessDto(
    val id: Int,
    val fullname: String,
    val username: String,
    val profession: String,
    val avatar: String?,

    @SerializedName("ratings_average")
    val ratingsAverage: Float,

    @SerializedName("ratings_count")
    val ratingsCount: Int,

    @SerializedName("distance_km")
    val distanceKm: Float?,

    val address: String?
)

data class LinkedProductsResponseDto(
    val business: LinkedProductsBusinessDto,
    val products: List<ProductDto>
)

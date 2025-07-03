package com.example.scrollbooker.entity.mapbbox.domain.model

data class Address(
    val fullAddress: String,
    val street: String,
    val city: String,
    val region: String,
    val country: String,
    val postalCode: String,
    val lat: Double,
    val lng: Double
)
package com.example.scrollbooker.entity.booking.business.domain.model

import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.products.domain.model.Product

data class BusinessSheet(
    val owner: BusinessOwner,
    val businessShortDomain: String,
    val address: String,
    val coordinates: BusinessCoordinates,
    val hasVideo: Boolean,
    val mediaFiles: List<BusinessMediaFile>,
    val products: List<Product>,
)
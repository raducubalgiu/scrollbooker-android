package com.example.scrollbooker.entity.booking.business.domain.model

import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.products.domain.model.Product

data class BusinessSheet(
    val business: BusinessSummary,
    val businessShortDomain: String,
    val address: String,
    val coordinates: BusinessCoordinates,
    val hasVideo: Boolean,
    val mediaPreview: BusinessMediaPreview?,
    val products: List<Product>
)
package com.example.scrollbooker.entity.booking.business.domain.model
import com.example.scrollbooker.core.enums.BusinessShortDomainEnum
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates

data class BusinessMarker(
    val owner: BusinessOwner,
    val businessShortDomain: BusinessShortDomainEnum,
    val address: String,
    val coordinates: BusinessCoordinates,
    val isPrimary: Boolean,
    val hasVideo: Boolean,
    val mediaPreview: BusinessMediaFile?,
)
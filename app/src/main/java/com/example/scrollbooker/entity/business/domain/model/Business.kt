package com.example.scrollbooker.entity.business.domain.model

import com.example.scrollbooker.entity.appointment.domain.model.BusinessCoordinates

data class Business(
    val id: Int,
    val businessTypeId: Int,
    val ownerId: Int,
    val description: String,
    val timezone: String,
    val address: String,
    val coordinates: BusinessCoordinates,
    val hasEmployees: Boolean
)
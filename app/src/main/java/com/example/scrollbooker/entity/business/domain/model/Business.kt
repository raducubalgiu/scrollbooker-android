package com.example.scrollbooker.entity.business.domain.model

data class Business(
    val id: Int,
    val businessTypeId: Int,
    val ownerId: Int,
    val description: String,
    val timezone: String,
    val address: String,
    val coordinates: List<Float>,
    val hasEmployees: Boolean
)
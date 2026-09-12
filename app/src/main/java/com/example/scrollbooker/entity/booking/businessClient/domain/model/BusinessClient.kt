package com.example.scrollbooker.entity.booking.businessClient.domain.model

data class BusinessClient(
    val id: Int,
    val businessId: Int,
    val userId: Int?,
    val fullname: String,
    val phone: String?,
)

data class BusinessClientCreate(
    val fullname: String,
    val phone: String?,
)

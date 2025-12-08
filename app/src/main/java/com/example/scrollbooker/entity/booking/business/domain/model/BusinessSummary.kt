package com.example.scrollbooker.entity.booking.business.domain.model

data class BusinessSummary(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val profession: String,
    val ratingsAverage: Float,
    val ratingsCount: Int
)
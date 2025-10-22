package com.example.scrollbooker.entity.booking.business.domain.model

data class RecommendedBusiness(
    val user: RecommendedBusinessUser,
    val distance: Float,
    val isOpen: Boolean
)

data class RecommendedBusinessUser(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val profession: String,
    val ratingsAverage: Float
)
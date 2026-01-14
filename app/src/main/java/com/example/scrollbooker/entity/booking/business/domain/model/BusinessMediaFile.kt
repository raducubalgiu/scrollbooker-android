package com.example.scrollbooker.entity.booking.business.domain.model

data class BusinessMediaFile(
    val url: String,
    val urlKey: String,
    val thumbnailUrl: String,
    val thumbnailKey: String,
    val type: String,
    val orderIndex: Int
)
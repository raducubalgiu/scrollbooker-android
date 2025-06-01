package com.example.scrollbooker.feature.myBusiness.domain.model

data class Schedule(
    val id: Int,
    val dayOfWeek: String,
    val userId: Int,
    val businessId: Int,
    val startTime: String,
    val endTime: String,
    val dayWeekNumber: Int
)
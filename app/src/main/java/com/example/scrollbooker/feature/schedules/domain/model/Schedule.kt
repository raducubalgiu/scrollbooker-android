package com.example.scrollbooker.feature.schedules.domain.model

data class Schedule(
    val id: Int,
    val dayOfWeek: String,
    val startTime: String?,
    val endTime: String?,
)
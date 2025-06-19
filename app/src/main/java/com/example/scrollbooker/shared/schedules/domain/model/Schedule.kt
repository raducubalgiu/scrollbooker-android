package com.example.scrollbooker.shared.schedules.domain.model

data class Schedule(
    val id: Int,
    val dayOfWeek: String,
    val startTime: String?,
    val endTime: String?,
)
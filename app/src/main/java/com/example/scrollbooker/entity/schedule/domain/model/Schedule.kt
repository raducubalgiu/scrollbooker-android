package com.example.scrollbooker.entity.schedule.domain.model

data class Schedule(
    val id: Int,
    val dayOfWeek: String,
    val startTime: String?,
    val endTime: String?,
)
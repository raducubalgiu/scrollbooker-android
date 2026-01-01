package com.example.scrollbooker.entity.user.userProfile.data.remote

import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule

data class UserProfileAboutDto(
    val description: String?,
    val address: String,
    val schedules: List<Schedule>
)
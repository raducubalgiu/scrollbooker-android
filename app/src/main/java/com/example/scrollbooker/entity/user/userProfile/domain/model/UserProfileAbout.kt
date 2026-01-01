package com.example.scrollbooker.entity.user.userProfile.domain.model

import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule

data class UserProfileAbout(
    val description: String?,
    val address: String,
    val schedules: List<Schedule>
)
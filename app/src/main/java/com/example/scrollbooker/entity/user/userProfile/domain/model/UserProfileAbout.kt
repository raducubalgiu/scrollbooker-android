package com.example.scrollbooker.entity.user.userProfile.domain.model

import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule

data class UserProfileAbout(
    val description: String?,
    val schedules: List<Schedule>,
    val location: BusinessLocation?,
    val ownerFullName: String
)
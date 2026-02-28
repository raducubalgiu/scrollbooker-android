package com.example.scrollbooker.entity.user.userProfile.data.remote

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessLocationDto
import com.example.scrollbooker.entity.booking.schedule.data.remote.ScheduleDto
import com.google.gson.annotations.SerializedName

data class UserProfileAboutDto(
    val description: String?,
    val schedules: List<ScheduleDto>,
    val location: BusinessLocationDto,

    @SerializedName("owner_fullname")
    val ownerFullName: String
)
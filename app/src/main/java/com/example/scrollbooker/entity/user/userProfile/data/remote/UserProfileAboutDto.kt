package com.example.scrollbooker.entity.user.userProfile.data.remote

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessLocationDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMediaFileDto
import com.example.scrollbooker.entity.booking.schedule.data.remote.ScheduleDto
import com.google.gson.annotations.SerializedName

data class UserProfileAboutDto(
    val description: String?,
    val schedules: List<ScheduleDto>,
    val location: BusinessLocationDto,

    val owner: UserProfileAboutOwnerDto,

    @SerializedName("business_media")
    val businessMedia: List<BusinessMediaFileDto>
)

data class UserProfileAboutOwnerDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val profession: String,
    val avatar: String?,

    @SerializedName("ratings_average")
    val ratingsAverage: Float
)
package com.example.scrollbooker.entity.user.userProfile.domain.model

import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessMediaFile
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.google.gson.annotations.SerializedName

data class UserProfileAbout(
    val description: String?,
    val schedules: List<Schedule>,
    val location: BusinessLocation,

    val owner: UserProfileAboutOwner,

    @SerializedName("business_media")
    val businessMedia: List<BusinessMediaFile>
)

data class UserProfileAboutOwner(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val profession: String,
    val avatar: String?,

    @SerializedName("ratings_average")
    val ratingsAverage: Float
)

package com.example.scrollbooker.feature.profile.data.remote

import com.google.gson.annotations.SerializedName

data class UserDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,
    val username: String,
    val bio: String,
    val profession: String,

    @SerializedName("instant_booking")
    val instantBooking: Boolean,

    @SerializedName("date_of_birth")
    val dateOfBirth: String,

    @SerializedName("phone_number")
    val phoneNumber: String
)
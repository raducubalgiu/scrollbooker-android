package com.example.scrollbooker.feature.profile.domain.model

data class User(
    val id: Int,
    val fullName: String,
    val username: String,
    val bio: String,
    val profession: String,
    val instantBooking: Boolean,
    val avatar: String,
    val dateOfBirth: String,
    val phoneNumber: String,
)

package com.example.scrollbooker.feature.user.domain.model

data class User(
    val id: Int,
    val fullName: String,
    val username: String,
    val email: String,
    val avatar: String?,
    val businessId: Int?,
    val businessTypeId: Int?,
    val counters: Counters,
    val bio: String?,
    val profession: String,
    val instantBooking: Boolean,
    val dateOfBirth: String?,
    val phoneNumber: String?
)

data class Counters(
    val followingsCount: Int,
    val followersCount: Int,
    val productsCount: Int,
    val postsCount: Int,
    val ratingsCount: Int,
    val ratingsAverage: Int
)
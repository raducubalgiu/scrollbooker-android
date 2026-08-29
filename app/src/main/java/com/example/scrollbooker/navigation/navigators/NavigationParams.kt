package com.example.scrollbooker.navigation.navigators

import com.example.scrollbooker.core.enums.BookingSourceEnum

data class UserProfileParam(
    val userId: Int,
    val username: String,
    val profession: String
)

data class ProfilePostDetailParam(
    val postTab: String,
    val postIndex: Int,
    val userId: Int,
)

data class ReviewsParam(
    val businessId: Int,
    val employeeId: Int?
)

data class ReviewsDetailParam(
    val reviewTab: String,
    val reviewIndex: Int
)

data class SocialParam(
    val tabIndex: Int,
    val userId: Int,
    val businessId: Int?,
    val employeeId: Int?,
    val username: String,
    val isBusinessOrEmployee: Boolean
)

data class BookingParam(
    val userId: Int,
    val businessId: Int,
    val businessOwnerId: Int,
    val source: BookingSourceEnum,
    val selectedProductId: Int? = null
)

data class CameraParams(
    val appointmentId: Int? = null,
    val businessOrEmployeeId: Int? = null
)
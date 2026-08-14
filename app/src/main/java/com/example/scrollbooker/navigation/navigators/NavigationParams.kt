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

data class NavigateSocialParam(
    val tabIndex: Int,
    val userId: Int,
    val username: String,
    val isBusinessOrEmployee: Boolean
)

data class NavigateBookingParam(
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
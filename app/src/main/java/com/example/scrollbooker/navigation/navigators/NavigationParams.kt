package com.example.scrollbooker.navigation.navigators

data class NavigateSocialParam(
    val tabIndex: Int,
    val userId: Int,
    val username: String,
    val isBusinessOrEmployee: Boolean
)

data class NavigateCalendarParam(
    val userId: Int,
    val slotDuration: Int,
    val productId: Int,
    val productName: String
)
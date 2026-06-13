package com.example.scrollbooker.navigation.navigators

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
    val source: String,
    val selectedProductId: Int? = null
)
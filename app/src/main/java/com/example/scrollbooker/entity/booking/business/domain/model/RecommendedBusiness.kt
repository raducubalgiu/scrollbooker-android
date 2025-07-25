package com.example.scrollbooker.entity.booking.business.domain.model
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

data class RecommendedBusiness(
    val user: UserSocial,
    val distance: Float,
    val isOpen: Boolean
)
package com.example.scrollbooker.entity.user.userProfile.domain.model
import java.math.BigDecimal

data class UserProfile(
    val id: Int,
    val username: String,
    val fullName: String,
    val avatar: String?,
    val gender: String,
    val bio: String?,
    val businessId: Int?,
    val businessTypeId: Int?,
    val counters: UserCounters,
    val profession: String,
    val openingHours: OpeningHours,
    val isFollow: Boolean,
    val businessOwner: BusinessOwner?
)

data class BusinessOwner(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val isFollow: Boolean
)

data class OpeningHours(
    val openNow: Boolean,
    val closingTime: String?,
    val nextOpenDay: String?,
    val nextOpenTime: String?
)

data class UserCounters(
    val userId: Int,
    val followingsCount: Int,
    val followersCount: Int,
    val productsCount: Int,
    val postsCount: Int,
    val ratingsCount: Int,
    val ratingsAverage: BigDecimal
)
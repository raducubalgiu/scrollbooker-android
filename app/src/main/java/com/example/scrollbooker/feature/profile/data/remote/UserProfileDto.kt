package com.example.scrollbooker.feature.profile.data.remote
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class UserProfileDto (
    val id: Int,
    val username: String,

    @SerializedName("fullname")
    val fullName: String,

    val avatar: String?,
    val gender: String,
    val bio: String?,

    @SerializedName("business_id")
    val businessId: Int?,

    @SerializedName("business_type_id")
    val businessTypeId: Int?,

    val counters: UserCountersDto,
    val profession: String,

    @SerializedName("opening_hours")
    val openingHours: OpeningHoursDto,

    @SerializedName("is_follow")
    val isFollow: Boolean,

    @SerializedName("business_owner")
    val businessOwner: BusinessOwnerDto?
)

data class BusinessOwnerDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,
    val username: String,
    val avatar: String?,

    @SerializedName("is_follow")
    val isFollow: Boolean
)

data class OpeningHoursDto(
    @SerializedName("open_now")
    val openNow: Boolean,

    @SerializedName("closing_time")
    val closingTime: String?,

    @SerializedName("next_open_day")
    val nextOpenDay: String?,

    @SerializedName("next_open_time")
    val nextOpenTime: String?
)

data class UserCountersDto(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("followings_count")
    val followingsCount: Int,

    @SerializedName("followers_count")
    val followersCount: Int,

    @SerializedName("products_count")
    val productsCount: Int,

    @SerializedName("posts_count")
    val postsCount: Int,

    @SerializedName("ratings_count")
    val ratingsCount: Int,

    @SerializedName("ratings_average")
    val ratingsAverage: BigDecimal
)
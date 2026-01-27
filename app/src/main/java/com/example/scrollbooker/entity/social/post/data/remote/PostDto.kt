package com.example.scrollbooker.entity.social.post.data.remote
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PostDto(
    val id: Int,
    val description: String?,

    val user: PostUserDto,

    @SerializedName("business_owner")
    val businessOwner: PostBusinessOwnerDto,

    val employee: PostEmployeeDto?,

    val counters: PostCountersDto,

    @SerializedName("user_actions")
    val userActions: UserPostActionsDto,

    val plan: BusinessPlanDto,

    @SerializedName("media_files")
    val mediaFiles: List<PostMediaFileDto> = emptyList(),

    val hashtags: List<HashtagDto>? = emptyList(),

    @SerializedName("is_video_review")
    val isVideoReview: Boolean,

    @SerializedName("is_own_post")
    val isOwnPost: Boolean,

    val rating: Int?,

    val bookable: Boolean,

    @SerializedName("business_id")
    val businessId: Int?,

    @SerializedName("last_minute")
    val lastMinute: LastMinuteDto,

    @SerializedName("created_at")
    val createdAt: String
)

data class BusinessPlanDto(
    val id: Int,
    val name: String
)

data class PostUserDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,

    @SerializedName("is_follow")
    val isFollow: Boolean,

    val profession: String,

    @SerializedName("ratings_average")
    val ratingsAverage: Float,

    @SerializedName("ratings_count")
    val ratingsCount: Int
)

data class PostBusinessOwnerDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val avatar: String?,

    @SerializedName("ratings_average")
    val ratingsAverage: Float
)

data class PostEmployeeDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val avatar: String?
)

data class PostProductDto(
    val id: Int,
    val name: String,
    val description: String?,
    val duration: Int,
    val price: BigDecimal,

    @SerializedName("price_with_discount")
    val priceWithDiscount: BigDecimal,

    val discount: BigDecimal,

    val currency: PostProductCurrencyDto
)

data class PostProductCurrencyDto(
    val id: Int,
    val name: String
)

data class UserPostActionsDto(
    @SerializedName("is_liked")
    val isLiked: Boolean,

    @SerializedName("is_bookmarked")
    val isBookmarked: Boolean,

    @SerializedName("is_reposted")
    val isReposted: Boolean
)

data class PostMediaFileDto(
    val id: Int,
    val url: String,
    val type: String,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,

    val duration: Float?,

    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("order_index")
    val orderIndex: Int
)

data class HashtagDto(
    val id: Int,
    val name: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

data class PostCountersDto(
    @SerializedName("comment_count")
    val commentCount: Int,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("bookmark_count")
    val bookmarkCount: Int,

    @SerializedName("share_count")
    val repostCount: Int,

    @SerializedName("bookings_count")
    val bookingsCount: Int,

    @SerializedName("views_count")
    val viewsCount: Int
)

data class FixedSlotsDto(
    @SerializedName("start_time")
    val startTime: String,

    @SerializedName("end_time")
    val endTime: String,

    @SerializedName("is_booked")
    val isBooked: Boolean
)

data class LastMinuteDto(
    @SerializedName("is_last_minute")
    val isLastMinute: Boolean,

    @SerializedName("last_minute_end")
    val lastMinuteEnd: String?,

    @SerializedName("has_fixed_slots")
    val hasFixedSlots: Boolean,

    @SerializedName("fixed_slots")
    val fixedSlots: List<FixedSlotsDto>? = emptyList()
)
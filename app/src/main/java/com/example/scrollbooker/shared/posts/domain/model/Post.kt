package com.example.scrollbooker.shared.posts.domain.model

import com.example.scrollbooker.feature.userSocial.data.remote.UserSocialDto
import java.math.BigDecimal

data class Post(
    val id: Int,
    val description: String?,
    val user: UserSocialDto,
    val product: PostProduct,
    val userActions: UserPostActions,
    val mediaFiles: List<PostMediaFile>,
    val counters: PostCounters,
    val mentions: List<UserSocialDto>,
    val hashtags: List<Hashtag>,
    val bookable: Boolean,
    val instantBooking: Boolean,
    val lastMinute: LastMinute,
    val createdAt: String
)

data class UserPostActions(
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val isReposted: Boolean
)

data class PostMediaFile(
    val id: Int,
    val url: String,
    val type: String,
    val thumbnailUrl: String,
    val duration: Float,
    val postId: Int,
    val orderIndex: Int
)

data class Hashtag(
    val id: Int,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)

data class PostProduct(
    val id: Int?,
    val name: String,
    val description: String?,
    val duration: Int,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val currency: String,
)

data class PostCounters(
    val commentCount: Int,
    val likeCount: Int,
    val bookmarkCount: Int,
    val shareCount: Int
)

data class FixedSlots(
    val startTime: String,
    val endTime: String,
    val isBooked: Boolean
)

data class LastMinute(
    val isLastMinute: Boolean,
    val lastMinuteEnd: String?,
    val hasFixedSlots: Boolean,
    val fixedSlots: List<FixedSlots>
)
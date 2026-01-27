package com.example.scrollbooker.entity.social.post.domain.model
import com.example.scrollbooker.R
import com.example.scrollbooker.core.enums.BusinessPlanEnum
import com.example.scrollbooker.ui.shared.posts.components.postOverlay.PostOverlayActionEnum
import java.math.BigDecimal

data class Post(
    val id: Int,
    val description: String?,
    val user: PostUser,
    val businessOwner: PostBusinessOwner,
    val employee: PostEmployee?,
    val userActions: UserPostActions,
    val plan: BusinessPlan,
    val mediaFiles: List<PostMediaFile>,
    val counters: PostCounters,
    val hashtags: List<Hashtag>,
    val isVideoReview: Boolean,
    val isOwnPost: Boolean,
    val rating: Int?,
    val bookable: Boolean,
    val businessId: Int?,
    val lastMinute: LastMinute,
    val createdAt: String
)

data class BusinessPlan(
    val id: Int,
    val name: BusinessPlanEnum?
)

data class PostUser(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val isFollow: Boolean,
    val profession: String,
    val ratingsAverage: Float,
    val ratingsCount: Int
)

data class PostBusinessOwner(
    val id: Int,
    val fullName: String,
    val avatar: String?,
    val ratingsAverage: Float
)

data class PostEmployee(
    val id: Int,
    val fullName: String,
    val avatar: String?
)

data class PostProduct(
    val id: Int,
    val name: String,
    val description: String?,
    val duration: Int,
    val price: BigDecimal,
    val priceWithDiscount: BigDecimal,
    val discount: BigDecimal,
    val currency: PostProductCurrency
)

data class PostProductCurrency(
    val id: Int,
    val name: String
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
    val duration: Float?,
    val postId: Int,
    val orderIndex: Int
)

data class Hashtag(
    val id: Int,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)

data class PostCounters(
    val commentCount: Int,
    val likeCount: Int,
    val bookmarkCount: Int,
    val repostCount: Int,
    val bookingsCount: Int,
    val viewsCount: Int
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

fun Post.showPhone(): Boolean = plan.name == BusinessPlanEnum.STANDARD

fun Post.ctaAction(): PostOverlayActionEnum = when {
    isVideoReview -> PostOverlayActionEnum.OPEN_BOOKINGS
    //product != null -> PostOverlayActionEnum.OPEN_BOOKINGS
    else -> PostOverlayActionEnum.OPEN_BOOKINGS
}

fun Post.ctaTitle(): Int = when {
    isVideoReview -> R.string.seeMore
    //product != null -> R.string.bookThisService
    else -> R.string.bookNow
}
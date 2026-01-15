package com.example.scrollbooker.entity.booking.business.domain.model
import com.example.scrollbooker.entity.booking.products.domain.model.Product
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.social.post.domain.model.BusinessPlan
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours
import org.threeten.bp.ZonedDateTime

data class BusinessProfile(
    val owner: BusinessProfileOwner,
    val openingHours: OpeningHours,
    val mediaFiles: List<BusinessMediaFile>,
    val businessPlan: BusinessPlan,
    val address: String,
    val distanceKm: Float?,
    val description: String?,
    val products: List<Product>,
    val employees: List<BusinessProfileEmployee>,
    val schedules: List<Schedule>,
    val reviews: BusinessProfileReviews
)

data class BusinessProfileOwner(
    val id: Int,
    val fullName: String,
    val username: String,
    val profession: String,
    val avatar: String?,
    val counters: BusinessProfileCounters,
    val isFollow: Boolean
)

data class BusinessProfileCounters(
    val followersCount: Int,
    val followingsCount: Int,
    val ratingsAverage: Float,
    val ratingsCount: Int
)

data class BusinessProfileEmployee(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val profession: String,
    val ratingsAverage: Float
)

data class BusinessProfileReviews(
    val total: Int,
    val data: List<BusinessProfileReview>
)

data class BusinessProfileReview(
    val id: Int,
    val review: String,
    val rating: Int,
    val reviewer: BusinessProfileReviewer,
    val createdAt: ZonedDateTime
)

data class BusinessProfileReviewer(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?
)
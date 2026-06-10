package com.example.scrollbooker.entity.booking.business.domain.model
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessLocationDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessMediaFileDto
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule
import com.example.scrollbooker.entity.social.post.domain.model.PostMediaFile
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours
import org.threeten.bp.ZonedDateTime

data class BusinessProfile(
    val id: Int,
    val owner: BusinessProfileOwner,
    val openingHours: OpeningHours,
    val mediaFiles: List<BusinessMediaFile>,
    val location: BusinessLocation,
    val distanceKm: Float?,
    val description: String?,
    val employees: List<BusinessProfileEmployee>,
    val schedules: List<Schedule>,
    val reviews: BusinessProfileReviews,
    val posts: List<BusinessProfileLatestPost>,
    val nearbyBusinesses: List<NearbyBusiness>,
)

data class BusinessProfileLatestPost(
    val id: Int,
    val businessId: Int,
    val user: BusinessProfileLatestPostUser,
    val viewsCount: Int,
    val mediaFiles: List<PostMediaFile>,
)

data class BusinessProfileLatestPostUser(
    val id: Int,
    val fullName: String,
    val username: String,
    val avatar: String?,
    val profession: String
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

data class BusinessLocation(
    val address: String,
    val formattedAddress: String,
    val coordinates: BusinessCoordinates,
    val mapUrl: String?
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

data class NearbyBusinessOwner(
    val id: Int,
    val fullName: String,
    val username: String,
    val profession: String,
    val avatar: String?,
    val counters: BusinessProfileCounters,
)

data class NearbyBusiness(
    val id: Int,
    val owner: NearbyBusinessOwner,
    val mediaFiles: List<BusinessMediaFileDto>,
    val location: BusinessLocationDto,
    val distanceKm: Float?,
)
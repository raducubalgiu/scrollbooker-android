package com.example.scrollbooker.entity.booking.business.data.remote
import com.example.scrollbooker.entity.booking.appointment.domain.model.BusinessCoordinates
import com.example.scrollbooker.entity.booking.schedule.data.remote.ScheduleDto
import com.example.scrollbooker.entity.social.post.data.remote.PostMediaFileDto
import com.example.scrollbooker.entity.user.userProfile.data.remote.OpeningHoursDto
import com.google.gson.annotations.SerializedName

data class BusinessProfileDto(
    val id: Int,

    val owner: BusinessProfileOwnerDto,

    @SerializedName("opening_hours")
    val openingHours: OpeningHoursDto,

    @SerializedName("media_files")
    val mediaFiles: List<BusinessMediaFileDto>,

    val location: BusinessLocationDto,

    @SerializedName("distance_km")
    val distanceKm: Float?,

    val description: String?,

    val employees: List<BusinessProfileEmployeeDto>,
    val schedules: List<ScheduleDto>,
    val reviews: BusinessProfileReviewsDto,
    val posts: List<BusinessProfileLatestPostDto>,

    @SerializedName("nearby_businesses")
    val nearbyBusinesses: List<NearbyBusinessDto>
)

data class BusinessProfileLatestPostDto(
    val id: Int,
    val businessId: Int,
    val user: BusinessProfileLatestPostUserDto,
    val viewsCount: Int,

    @SerializedName("media_files")
    val mediaFiles: List<PostMediaFileDto>,
)

data class BusinessProfileLatestPostUserDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,
    val profession: String
)

data class BusinessProfileOwnerDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,
    val username: String,
    val profession: String,
    val avatar: String?,
    val counters: BusinessProfileCountersDto,

    @SerializedName("is_follow")
    val isFollow: Boolean
)

data class BusinessLocationDto(
    val address: String,

    @SerializedName("formatted_address")
    val formattedAddress: String,

    val coordinates: BusinessCoordinates,

    @SerializedName("map_url")
    val mapUrl: String?
)

data class BusinessProfileCountersDto(
    @SerializedName("followers_count")
    val followersCount: Int,

    @SerializedName("followings_count")
    val followingsCount: Int,

    @SerializedName("ratings_average")
    val ratingsAverage: Float,

    @SerializedName("ratings_count")
    val ratingsCount: Int
)

data class BusinessProfileEmployeeDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?,
    val profession: String,

    @SerializedName("ratings_average")
    val ratingsAverage: Float
)

data class BusinessProfileReviewsDto(
    val total: Int,
    val data: List<BusinessProfileReviewDto>
)

data class BusinessProfileReviewDto(
    val id: Int,
    val review: String,
    val rating: Int,
    val reviewer: BusinessProfileReviewerDto,

    @SerializedName("created_at")
    val createdAt: String
)

data class BusinessProfileReviewerDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,

    val username: String,
    val avatar: String?
)

data class NearbyBusinessOwnerDto(
    val id: Int,

    @SerializedName("fullname")
    val fullName: String,
    val username: String,
    val profession: String,
    val avatar: String?,
    val counters: BusinessProfileCountersDto,
)

data class NearbyBusinessDto(
    val id: Int,
    val owner: NearbyBusinessOwnerDto,

    @SerializedName("media_files")
    val mediaFiles: List<BusinessMediaFileDto>,

    val location: BusinessLocationDto,

    @SerializedName("distance_km")
    val distanceKm: Float?,
)
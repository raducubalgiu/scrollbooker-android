package com.example.scrollbooker.entity.booking.business.data.remote
import com.example.scrollbooker.entity.booking.products.data.remote.ProductDto
import com.example.scrollbooker.entity.booking.schedule.data.remote.ScheduleDto
import com.example.scrollbooker.entity.social.post.data.remote.BusinessPlanDto
import com.example.scrollbooker.entity.user.userProfile.data.remote.OpeningHoursDto
import com.google.gson.annotations.SerializedName

data class BusinessProfileDto(
    val owner: BusinessProfileOwnerDto,

    @SerializedName("opening_hours")
    val openingHours: OpeningHoursDto,

    @SerializedName("media_files")
    val mediaFiles: List<BusinessMediaFileDto>,

    @SerializedName("business_plan")
    val businessPlan: BusinessPlanDto,

    val address: String,

    @SerializedName("distance_km")
    val distanceKm: Float?,

    val description: String?,

    val products: List<ProductDto>,
    val employees: List<BusinessProfileEmployeeDto>,
    val schedules: List<ScheduleDto>,
    val reviews: List<BusinessProfileReviewDto>
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

data class BusinessProfileReviewDto(
    val id: Int,
    val review: String,
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
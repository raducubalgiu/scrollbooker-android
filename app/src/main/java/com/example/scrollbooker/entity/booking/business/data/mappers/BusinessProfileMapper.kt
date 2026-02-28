package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.BusinessLocationDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessProfileCountersDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessProfileDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessProfileEmployeeDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessProfileOwnerDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessProfileReviewDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessProfileReviewerDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessProfileReviewsDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessLocation
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfile
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileCounters
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileEmployee
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileOwner
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileReview
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileReviewer
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessProfileReviews
import com.example.scrollbooker.entity.booking.products.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.schedule.data.mappers.toDomain
import com.example.scrollbooker.entity.social.post.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userProfile.data.mappers.toDomain
import org.threeten.bp.ZonedDateTime

fun BusinessProfileDto.toDomain(): BusinessProfile {
    return BusinessProfile(
        owner = owner.toDomain(),
        openingHours = openingHours.toDomain(),
        mediaFiles = mediaFiles.map { it.toDomain() },
        businessPlan = businessPlan.toDomain(),
        location = location.toDomain(),
        distanceKm = distanceKm,
        description = description,
        products = products.map { it.toDomain() },
        employees = employees.map { it.toDomain() },
        schedules = schedules.map { it.toDomain() },
        reviews = reviews.toDomain()
    )
}

fun BusinessProfileOwnerDto.toDomain(): BusinessProfileOwner {
    return BusinessProfileOwner(
        id = id,
        fullName = fullName,
        username = username,
        profession = profession,
        avatar = avatar,
        counters = counters.toDomain(),
        isFollow = isFollow
    )
}

fun BusinessLocationDto.toDomain(): BusinessLocation {
    return BusinessLocation(
        address = address,
        coordinates = coordinates,
        mapUrl = mapUrl
    )
}

fun BusinessProfileCountersDto.toDomain(): BusinessProfileCounters {
    return BusinessProfileCounters(
        followersCount = followersCount,
        followingsCount = followingsCount,
        ratingsAverage = ratingsAverage,
        ratingsCount = ratingsCount
    )
}

fun BusinessProfileEmployeeDto.toDomain(): BusinessProfileEmployee {
    return BusinessProfileEmployee(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        profession = profession,
        ratingsAverage = ratingsAverage
    )
}

fun BusinessProfileReviewsDto.toDomain(): BusinessProfileReviews {
    return BusinessProfileReviews(
        total = total,
        data = data.map { it.toDomain() }
    )
}

fun BusinessProfileReviewDto.toDomain(): BusinessProfileReview {
    return BusinessProfileReview(
        id = id,
        review = review,
        rating = rating,
        reviewer = reviewer.toDomain(),
        createdAt = ZonedDateTime.parse(createdAt)
    )
}

fun BusinessProfileReviewerDto.toDomain(): BusinessProfileReviewer {
    return BusinessProfileReviewer(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar
    )
}
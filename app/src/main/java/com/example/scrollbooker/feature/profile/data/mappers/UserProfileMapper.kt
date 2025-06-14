package com.example.scrollbooker.feature.profile.data.mappers
import com.example.scrollbooker.feature.profile.data.remote.OpeningHoursDto
import com.example.scrollbooker.feature.profile.data.remote.UserCountersDto
import com.example.scrollbooker.feature.profile.data.remote.UserProfileDto
import com.example.scrollbooker.feature.profile.domain.model.OpeningHours
import com.example.scrollbooker.feature.profile.domain.model.UserCounters
import com.example.scrollbooker.feature.profile.domain.model.UserProfile

fun UserProfileDto.toDomain(): UserProfile {
    return UserProfile(
        id = id,
        username = username,
        fullName = fullName,
        avatar = avatar,
        gender = gender,
        bio = bio,
        businessId = businessId,
        businessTypeId = businessTypeId,
        counters = counters.toDomain(),
        profession = profession,
        openingHours = openingHours.toDomain()
    )
}

fun UserCountersDto.toDomain(): UserCounters {
    return UserCounters(
        userId = userId,
        followingsCount = followingsCount,
        followersCount = followersCount,
        productsCount = productsCount,
        postsCount = postsCount,
        ratingsCount = ratingsCount,
        ratingsAverage = ratingsAverage
    )
}

fun OpeningHoursDto.toDomain(): OpeningHours {
    return OpeningHours(
        openNow = openNow,
        closingTime = closingTime,
        nextOpenDay = nextOpenDay,
        nextOpenTime = nextOpenTime
    )
}
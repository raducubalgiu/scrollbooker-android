package com.example.scrollbooker.entity.user.userProfile.data.mappers

import com.example.scrollbooker.entity.user.userProfile.data.remote.BusinessOwnerDto
import com.example.scrollbooker.entity.user.userProfile.data.remote.OpeningHoursDto
import com.example.scrollbooker.entity.user.userProfile.data.remote.UserCountersDto
import com.example.scrollbooker.entity.user.userProfile.data.remote.UserProfileDto
import com.example.scrollbooker.entity.user.userProfile.domain.model.BusinessOwner
import com.example.scrollbooker.entity.user.userProfile.domain.model.OpeningHours
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserCounters
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfile

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
        openingHours = openingHours.toDomain(),
        isFollow = isFollow,
        businessOwner = businessOwner?.toDomain(),
        isOwnProfile = isOwnProfile,
        isBusinessOrEmployee = isBusinessOrEmployee,
        distanceKm = distanceKm,
        address = address
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

fun BusinessOwnerDto.toDomain(): BusinessOwner {
    return BusinessOwner(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        isFollow = isFollow
    )
}
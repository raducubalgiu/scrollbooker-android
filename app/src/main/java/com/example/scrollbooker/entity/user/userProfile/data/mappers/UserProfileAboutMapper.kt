package com.example.scrollbooker.entity.user.userProfile.data.mappers

import com.example.scrollbooker.entity.booking.business.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.schedule.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userProfile.data.remote.UserProfileAboutDto
import com.example.scrollbooker.entity.user.userProfile.data.remote.UserProfileAboutOwnerDto
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAboutOwner

fun UserProfileAboutDto.toDomain(): UserProfileAbout {
    return UserProfileAbout(
        description = description,
        schedules = schedules.map { it.toDomain() },
        location = location.toDomain(),
        owner = owner.toDomain(),
        businessMedia = businessMedia.map { it.toDomain() }
    )
}

fun UserProfileAboutOwnerDto.toDomain(): UserProfileAboutOwner {
    return UserProfileAboutOwner(
        id = id,
        fullName = fullName,
        username = username,
        profession = profession,
        avatar = avatar,
        ratingsAverage = ratingsAverage
    )
}
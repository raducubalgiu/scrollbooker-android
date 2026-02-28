package com.example.scrollbooker.entity.user.userProfile.data.mappers

import com.example.scrollbooker.entity.booking.business.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.schedule.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userProfile.data.remote.UserProfileAboutDto
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout

fun UserProfileAboutDto.toDomain(): UserProfileAbout {
    return UserProfileAbout(
        description = description,
        schedules = schedules.map { it.toDomain() },
        location = location.toDomain(),
        ownerFullName = ownerFullName
    )
}
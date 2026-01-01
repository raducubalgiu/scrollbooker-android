package com.example.scrollbooker.entity.user.userProfile.data.mappers

import com.example.scrollbooker.entity.user.userProfile.data.remote.UserProfileAboutDto
import com.example.scrollbooker.entity.user.userProfile.domain.model.UserProfileAbout

fun UserProfileAboutDto.toDomain(): UserProfileAbout {
    return UserProfileAbout(
        description = description,
        address = address,
        schedules = schedules
    )
}
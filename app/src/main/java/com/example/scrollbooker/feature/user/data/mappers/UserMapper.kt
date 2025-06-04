package com.example.scrollbooker.feature.user.data.mappers

import com.example.scrollbooker.feature.user.data.remote.CountersDto
import com.example.scrollbooker.feature.user.data.remote.UserDto
import com.example.scrollbooker.feature.user.domain.model.Counters
import com.example.scrollbooker.feature.user.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        fullName = fullName,
        username = username,
        email = email,
        avatar = avatar,
        businessId = businessId,
        businessTypeId = businessTypeId,
        counters = counters.toDomain(),
        bio = bio,
        profession = profession,
        instantBooking = instantBooking,
        dateOfBirth = dateOfBirth,
        phoneNumber = phoneNumber,
    )
}

fun CountersDto.toDomain(): Counters {
    return Counters(
        followingsCount = followingsCount,
        followersCount = followersCount,
        productsCount = productsCount,
        postsCount = postsCount,
        ratingsCount = ratingsCount,
        ratingsAverage = ratingsAverage
    )
}
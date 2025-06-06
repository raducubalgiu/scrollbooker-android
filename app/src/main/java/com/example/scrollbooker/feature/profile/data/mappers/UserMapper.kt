package com.example.scrollbooker.feature.profile.data.mappers
import com.example.scrollbooker.feature.profile.data.remote.UserDto
import com.example.scrollbooker.feature.profile.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        fullName = fullName,
        username = username,
        bio = bio,
        profession = profession,
        instantBooking = instantBooking,
        avatar = avatar,
        dateOfBirth = dateOfBirth,
        phoneNumber = phoneNumber
    )
}
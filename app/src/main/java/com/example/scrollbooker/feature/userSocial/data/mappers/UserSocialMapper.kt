package com.example.scrollbooker.feature.userSocial.data.mappers
import com.example.scrollbooker.feature.userSocial.data.remote.UserSocialDto
import com.example.scrollbooker.feature.userSocial.domain.model.UserSocial

fun UserSocialDto.toDomain(): UserSocial {
    return UserSocial(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        isFollow = isFollow,
    )
}
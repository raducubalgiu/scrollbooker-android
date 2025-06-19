package com.example.scrollbooker.screens.profile.userSocial.data.mappers
import com.example.scrollbooker.screens.profile.userSocial.data.remote.UserSocialDto
import com.example.scrollbooker.screens.profile.userSocial.domain.model.UserSocial

fun UserSocialDto.toDomain(): UserSocial {
    return UserSocial(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        isFollow = isFollow,
        profession = profession
    )
}
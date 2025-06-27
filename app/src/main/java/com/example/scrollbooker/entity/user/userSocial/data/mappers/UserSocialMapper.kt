package com.example.scrollbooker.entity.user.userSocial.data.mappers

import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.example.scrollbooker.entity.user.userSocial.domain.model.UserSocial

fun UserSocialDto.toDomain(): UserSocial {
    return UserSocial(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        isFollow = isFollow,
        profession = profession,
        isBusinessOrEmployee = isBusinessOrEmployee
    )
}
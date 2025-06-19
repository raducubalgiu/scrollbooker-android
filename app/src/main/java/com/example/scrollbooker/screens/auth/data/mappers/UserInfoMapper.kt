package com.example.scrollbooker.screens.auth.data.mappers

import com.example.scrollbooker.screens.auth.data.remote.userInfo.UserInfoDto
import com.example.scrollbooker.screens.auth.domain.model.UserInfo

fun UserInfoDto.toDomain(): UserInfo {
    return UserInfo(
        id = id,
        businessId = businessId,
        businessTypeId = businessTypeId
    )
}
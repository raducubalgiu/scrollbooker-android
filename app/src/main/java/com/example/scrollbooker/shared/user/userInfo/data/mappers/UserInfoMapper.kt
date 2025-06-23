package com.example.scrollbooker.shared.user.userInfo.data.mappers

import com.example.scrollbooker.shared.user.userInfo.data.remote.UserInfoDto
import com.example.scrollbooker.shared.user.userInfo.domain.model.UserInfo

fun UserInfoDto.toDomain(): UserInfo {
    return UserInfo(
        id = id,
        businessId = businessId,
        businessTypeId = businessTypeId
    )
}
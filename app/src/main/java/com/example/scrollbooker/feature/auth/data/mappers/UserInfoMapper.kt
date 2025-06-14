package com.example.scrollbooker.feature.auth.data.mappers

import com.example.scrollbooker.feature.auth.data.remote.UserInfoDto
import com.example.scrollbooker.feature.auth.domain.model.UserInfo

fun UserInfoDto.toDomain(): UserInfo {
    return UserInfo(
        id = id,
        businessId = businessId,
        businessTypeId = businessTypeId
    )
}
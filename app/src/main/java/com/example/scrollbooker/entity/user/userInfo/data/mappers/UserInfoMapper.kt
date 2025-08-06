package com.example.scrollbooker.entity.user.userInfo.data.mappers

import com.example.scrollbooker.entity.user.userInfo.data.remote.UserInfoDto
import com.example.scrollbooker.entity.user.userInfo.domain.model.RegistrationStepEnum
import com.example.scrollbooker.entity.user.userInfo.domain.model.UserInfo

fun UserInfoDto.toDomain(): UserInfo {
    return UserInfo(
        id = id,
        username = username,
        fullname = fullname,
        businessId = businessId,
        businessTypeId = businessTypeId,
        isValidated = isValidated,
        registrationStep = RegistrationStepEnum.fromKeyOrNull(registrationStep)
    )
}